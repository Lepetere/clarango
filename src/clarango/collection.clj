(ns clarango.collection
  (:require [clarango.utilities.http-utility :as http])
  (:use [clarango.utilities.core-utility :only [remove-map filter-out-map]]
        [clarango.utilities.uri-utility :only [build-ressource-uri]]))

(defn get-all-documents
  "Returns a list with the URIs of all documents in the collection.

  Can be called without arguments. In that case the default collection from the default database will be used.
  Optionally you can pass a collection name as first and a database name as second argument."
  [& args]
  (http/get-uri [:body "documents"] (apply build-ressource-uri "document/?collection=" nil (remove-map args))))

(defn get-info
  "Returns information about a collection.

  Can be called without arguments. In that case the default collection from the default database will be used.
  Optionally you can pass a collection name as first and a database name as second argument."
  [& args]
  (http/get-uri [:body] (apply build-ressource-uri "collection" nil (remove-map args)) (filter-out-map args)))

(defn get-extended-info
  "Returns extended information about a collection. Forces a load of the collection.

  Can be called without arguments. In that case the default collection from the default database will be used.
  Optionally you can pass a collection name as first and a database name as second argument."
  [& args]
  (http/get-uri [:body] (apply build-ressource-uri "collection" "properties" (remove-map args)) (filter-out-map args)))

(defn get-extended-info-count
  "Returns extended information about a collection including the number of documents in the collection.
  Forces a load of the collection.

  Can be called without arguments. In that case the default collection from the default database will be used.
  Optionally you can pass a collection name as first and a database name as second argument."
  [& args]
  (http/get-uri [:body] (apply build-ressource-uri "collection" "count" (remove-map args)) (filter-out-map args)))

(defn get-extended-info-figures
  "Returns extended information about a collection including detailed information about the documents in the collection.
  Forces a load of the collection.

  Can be called without arguments. In that case the default collection from the default database will be used.
  Optionally you can pass a collection name as first and a database name as second argument."
  [& args]
  (http/get-uri [:body] (apply build-ressource-uri "collection" "figures" (remove-map args)) (filter-out-map args)))

(defn create
  "Creates a new collection.

  Takes the name of the new collection as first argument.

  Takes optionally a database name and a map containing options as further arguments. 
  These arguments may be passed in arbituary order.
  If the database name is omitted by the user, the default db will be used.

  Possible options in the options map are:
  {'waitForSync' true/false, 'doCompact' true/false, 'journalSize' journal_size, 'isSystem' true/false, 
  'isVolatile' true/false, 'type' 2/3, 'keyOptions' [...see below...]} (replace the single quotes with double quotes)
  - waitForSync meaning if the server response should wait until the document is saved to disk
  - doCompact meaning whether of not the collection will be compacted (default is true)
  - journalSize is the maximum size of a journal or datafile; must at least be 1 MB; this can limit also the maximum size of a single object
  - isSystem meaning if a system collection should be created (default is false)
  - isVolatile meaning if the collection should only be kept in-memory and not made persistent
    --> keeping the collection in-memory only will make it slightly faster, but restarting the server will cause full loss
  - type is the type of the collection: 2 = document collection (default), 3 = edges collection
  - keyOptions: a JSON array containing the following options for key generation:
      - type is the type of the key generator (currently available are 'traditional' and 'autoincrement')
      - allowUserKeys true/false means if true the user can supply his own keys on creating a document; 
          when set to false only the key generator will be responsible for creating the keys;
      - increment is the increment value for the autoincrement key generator (optional)
      - offset is the initial offset value for the autoincrement key generator (optional)"
  [collection-name & args]
  (http/post-uri [:body] (apply build-ressource-uri "collection" nil nil (remove-map args)) (merge {:name collection-name} (filter-out-map args))))

(defn truncate
  "Removes all documents from a collection, but leaves the indexes intact.

  Can be called without arguments. In that case the default collection from the default database will be truncated.
  Optionally you can pass a collection name as first and a database name as second argument."
  [& args]
  (http/put-uri [:body] (apply build-ressource-uri "collection" "truncate" args)))

(defn delete
  "Deletes a collection.

  Takes the name of the collection to be deleted as first argument.
  Optionally you can pass a database name as second argument."
  [collection-name & args]
  (http/delete-uri [:body] (apply build-ressource-uri "collection" nil collection-name args)))

;; Hides .../load -> Find a different name?
(defn load
  "Loads a collection into the memory. Returns the collection on success. (?)

  Can be called without arguments. In that case the default collection from the default database will be loaded.
  Optionally you can pass a collection name, a database name and a map with options as arguments.
  Possible options in the options map are:
  {'count' true/false}
  - count meaning if the return value should contain the number of documents in the collection
    -> the default is true, but setting it to false may speed up the request

  The option map might be passed in an arbitrary position between the other arguments."
  [& args]
  (http/put-uri [:body] (apply build-ressource-uri "collection" "load" (remove-map args)) (filter-out-map args)))

(defn unload
  "Removes a collection from the memory. On success a map containing collection properties is returned.

  Can be called without arguments. In that case the default collection from the default database will be truncated.
  Optionally you can pass a collection name as first and a database name as second argument."
  [& args]
  (http/put-uri [:body] (apply build-ressource-uri "collection" "unload" (remove-map args)) (filter-out-map args)))

(defn modify-properties ;; or update-properties?
  "Modifies  the properties of a collection.

  As first argument expects a map with options.
  Takes optional a collection name and a db name as further arguments.
  If omitted by user, the default db and collection will be used.

  Possible options in the options map are:
  {'waitForSync' true/false 'journalSize' size}
  - waitForSync meaning if the server response should wait until the document is saved to disk
  - journalSize is the size (in bytes) for new journal files that are created for the collection"
  [properties & args]
  (http/put-uri [:body] (apply build-ressource-uri "collection" "properties" (remove-map args)) (filter-out-map args)))

;; Hides clojure.set/rename -> Find a different name?
(defn rename
  "Renames a collection. On success return a map with properties.

  First argument: The new collection name
  Second argument: The old collection name

  Takes optional a db name as further argument.
  If omitted by user, the default db will be used."
  [new-name collection-name & args]
  (http/put-uri [:body] (apply build-ressource-uri "collection" "rename" collection-name args) {:name new-name}))

(defn rotate
  "Rotates the journal of a collection. 
  This means the current journal of the collection will be closed and all
  data made read-only in order to compact it. New documents will be stored in a new journal.

  Can be called without arguments. In that case the default collection from the default database will be rotated.
  Optionally you can pass a collection name as first and a database name as second argument."
  [& args]
  (http/put-uri [:body] (apply build-ressource-uri "collection" "rotate" args)))