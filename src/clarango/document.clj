(ns clarango.document
  (:require [clarango.core :as clarango.core]
            [clarango.utilities.http-utility :as http])
  (:use [clarango.utilities.core-utility :only [remove-map filter-out-map filter-out-collection-name filter-out-database-name]]
        [clarango.utilities.uri-utility :only [build-ressource-uri]]))

(defn get-by-key
  "Gets a document by its key.

  Takes the document key as first argument. 

  Takes optional a collection name and a db name as further arguments.
  If omitted by user, the default db and collection will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id} (replace the single quotes with double quotes)
  - rev is the document revision; if the current document revision_id does not match the given one, an error is thrown
  The option map might be passed in an arbitrary position after the first two arguments."
  [& args]
  (http/get-uri [:body] (apply build-ressource-uri "document" (remove-map args)) (filter-out-map args)))

(defn get-by-example
  "Gets a document or a number of documents out of a collection by giving an example to match.

  Takes the example as a map as first argument. 

  Takes optional a collection name and a db name as further arguments.
  If omitted by user, the default db and collection will be used.

  Also optional as argument is another map containing further options:
  {'skip' skip, 'limit' limit} (replace the single quotes with double quotes)
  - skip meaning the (number of?) documents to skip in the result
  - limit meaning the maximum amount of documents to return
  The option map might be passed in an arbitrary position after the first two arguments."
  [example & args] ; what happens here if there is a db explicitely passed to this method? Do we nee a filterout-db-name-from-args too?
  (http/put-uri [:body "result"] (build-ressource-uri "simple/by-example" nil nil) (merge {:example example :collection (filter-out-collection-name args)} (filter-out-map args))))

(defn get-first-by-example
  "Gets the first document out of a collection that matches an example.

  Takes the example as a map as first argument. 

  Takes optional a collection name and a db name as further arguments.
  If omitted by user, the default db and collection will be used."
  [example & args]  ; what happens here if there is a db explicitely passed to this method? Do we nee a filterout-db-name-from-args too?
  (http/put-uri [:body "document"] (build-ressource-uri "simple/first-example" nil nil) {:example example :collection (filter-out-collection-name args)}))

(defn get-info
  "Gets information about a document by its key.

  Takes the document key as first argument. 

  Takes optional a collection name and a db name as further arguments.
  If omitted by user, the default db and collection will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id, 'policy' 'error/last'} (replace the single quotes with double quotes)
  - rev is the document revision
  - policy meaning the desired behaviour in case the given revision number does not match the latest document revision
    -> 'error' meaning that an error is thrown if the given revision_id does not match the revision_id in the document
    -> 'last' meaning the document is still returned even if the given revision_id does not match the revision_id in the document
  The option map might be passed in an arbitrary position after the first two arguments."
  [& args]
  (http/head-uri [:headers] (apply build-ressource-uri "document" (remove-map args)) (filter-out-map args)))

(defn create
  "Creates a document. 

  First argument: A map that represents the document. 
  If you want to specify a key by yourself, add it as the :_key parameter to the document map. 
  If you would like the key to be created automatically, just leave this parameter out.

  Takes optional a collection name and a db name as further arguments.
  If omitted by user, the default db and collection will be used.

  Also optional as argument is another map containing further options:
  {'createCollection' true/false, 'waitForSync' true/false} (replace the single quotes with double quotes)
  - createCollection meaning if the collection should be created if it does not exist yet;
  - waitForSync meaning if the server response should wait until the document is saved to disk;
  The option map might be passed in an arbitrary position after the first argument."
  [document & args]
  ;; what about the document key if the user desires to specify it by himself? 
  ;; Should he just pass it in the json document? or allow it as optional argument?
  (http/post-uri [:body] (apply build-ressource-uri "document/?collection=" nil (remove-map args)) document (filter-out-map args)))

(defn create-multi
  "Creates multiple documents at a time.

  First argument is a vector of documents.

  Takes optional a collection name and a db name as further arguments.
  If omitted by user, the default db and collection will be used."
  [documents & args]
  ;; what about the document key if the user desires to specify it by himself? 
  ;; Should he just pass it in the json document? or allow it as optional argument?
  (http/post-multi-uri [:body] (build-ressource-uri "batch") documents (filter-out-collection-name args) (filter-out-database-name args)))

(defn replace-by-key
  "Replaces a document with a map representing the new document.

  First argument: A map representing the new document.
  Second argument: The document key.

  Takes optional a collection name and a db name as further arguments.
  If omitted by user, the default db and collection will be used.

  Also optional as argument is another map containing further options:
  {'waitForSync' true/false, 'rev' revision_id, 'policy' 'error/last'} (replace the single quotes with double quotes)
  - waitForSync meaning if the server response should wait until the document is saved to disk
  - rev is the document revision
  - policy meanins the desired behaviour in case the given revision number does not match the latest document revision
    -> 'error' meaning that an error is thrown if the given revision_id does not match the revision_id in the document
    -> 'last' meaning the document is still replaced even if the given revision_id does not match the revision_id in the document
  The option map might be passed in an arbitrary position after the first two arguments."
  [new-document & args]
  (http/put-uri [:body] (apply build-ressource-uri "document" (remove-map args)) new-document (filter-out-map args)))

(defn replace-by-example
  "Replaces a document or a number of documents out of a collection by giving an example to match.

  First argument: A map representing the new document.
  Second argument: The example map.

  Takes optional a collection name and a db name as further arguments.
  If omitted by user, the default db and collection will be used.

  Also optional as argument is another map containing further options:
  {'waitForSync' true/false, 'limit' limit} (replace the single quotes with double quotes)
  - waitForSync meaning if the server response should wait until the document is saved to disk
  - limit meaning the maximum amount of documents that will be replaced
  The option map might be passed in an arbitrary position after the first two arguments."
  [new-document example & args]
  (http/put-uri [:body] (build-ressource-uri "simple/replace-by-example" nil nil) (merge {:example example :newValue new-document :collection (filter-out-collection-name args)} (filter-out-map args))))

(defn update-by-key
  "Updates a document with a number of key value pairs. Inserts them into the existing document.

  First argument: A map containing the new key/value pairs.
  Second argument: The document key.

  Takes optional a collection name and a db name as further arguments.
  If omitted by user, the default db and collection will be used.

  Also optional as argument is another map containing further options:
  {'waitForSync' true/false, 'keepNull' true/false, 'rev' revision_id, 'policy' 'error/last'} (replace the single quotes with double quotes)
  - waitForSync meaning if the server response should wait until the document is saved to disk;
  - keepNull meaning if the key/value pair should be deleted in the document
    if the argument map contains it with a null as value;
  - rev is the document revision
  - policy meanins the desired behaviour in case the given revision number does not match the latest document revision
    -> 'error' meaning that an error is thrown if the given revision_id does not match the revision_id in the document
    -> 'last' meaning the document is still updated even if the given revision_id does not match the revision_id in the document
  The option map might be passed in an arbitrary position after the first two arguments."
  [document & args]
  (http/patch-uri [:body] (apply build-ressource-uri "document" (remove-map args)) document (filter-out-map args)))

(defn update-by-example
  "Updates a document or a number of documents out of a collection by giving an example to match.

  First argument: A map containing the new key/value pairs.
  Second argument: The example map.

  Takes optional a collection name and a db name as further arguments.
  If omitted by user, the default db and collection will be used.

  Also optional as argument is another map containing further options:
  {'waitForSync' true/false, 'limit' limit, 'keepNull' true/false} (replace the single quotes with double quotes)
  - waitForSync meaning if the server response should wait until the document is saved to disk
  - limit meaning the maximum amount of documents that will be updated
  - keepNull meaning if the key/value pair should be deleted in the document
  The option map might be passed in an arbitrary position after the first two arguments."
  [document example & args]
  (http/put-uri [:body] (build-ressource-uri "simple/update-by-example" nil nil) (merge {:example example :newValue document :collection (filter-out-collection-name args)} (filter-out-map args))))

(defn delete-by-key
  "Deletes a document by its id.

  Takes the document key as first argument. 

  Takes optional a collection name and a db name as further arguments.
  If omitted by user, the default db and collection will be used.

  Also optional as argument is another map containing further options:
  {'waitForSync' true/false, 'rev' revision_id, 'policy' 'error/last'} (replace the single quotes with double quotes)
  - waitForSync meaning if the server response should wait until the document is saved to disk;
  - rev is the document revision
  - policy meanins the desired behaviour in case the given revision number does not match the latest document revision
    -> 'error' meaning that an error is thrown if the given revision_id does not match the revision_id in the document
    -> 'last' meaning the document is still deleted even if the given revision_id does not match the revision_id in the document
  The option map might be passed in an arbitrary position after the first argument."
  [& args]
  (http/delete-uri [:body] (apply build-ressource-uri "document" (remove-map args)) (filter-out-map args)))

(defn delete-by-example
  "Deletes a document or a number of documents out of a collection by giving an example to match.

  Takes the example as a map as first argument. 

  Takes optional a collection name and a db name as further arguments.
  If omitted by user, the default db and collection will be used.

  Also optional as argument is another map containing further options:
  {'waitForSync' true/false, 'limit' limit} (replace the single quotes with double quotes)
  - waitForSync meaning if the server response should wait until the document is saved to disk
  - limit meaning the maximum amount of documents that will be deleted
  The option map might be passed in an arbitrary position after the first two arguments."
  [example & args]
  (http/put-uri [:body] (build-ressource-uri "simple/remove-by-example" nil nil) (merge {:example example :collection (filter-out-collection-name args)} (filter-out-map args))))