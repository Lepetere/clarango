;; Please note:
;; The methods in this namespace are only intended for internal use.

(ns clarango.utilities.uri-utility
	(:use [clarango.utilities.core-utility :only 
    [get-safe-connection-url get-default-db get-default-collection-name get-default-graph-name]]))

(defn get-default-collection-or-graph
  "In case the type parameter is 'graph', returns the default graph that was set in core.
  In all other cases returns the default collection."
  [type]
  (if (or (= type "graph") (= type "gharial")) (get-default-graph-name) (get-default-collection-name)))

(defn- extract-db-or-collection-name
  "Takes a piece of data and extracts the :collection-name or :database-name from its metadata, if present.
  If not present, throws an error."
  [data]
  (println "extract-db-or-collection-name from" data)
  (if (contains? (meta data) :database-name)
    (:database-name (meta data))
    (if (contains? (meta data) :collection-name)
      (:collection-name (meta data))
      (throw (Exception. "Map passed to build url string does not contain a database-name or collection-name in its metadata. Please check your call arguments.")))))

(defn connect-url-parts
  "Builds a string out of different parts. Adds a '/' between the string parts if not present.
  Apart from strings or keywords this method accepts also maps as arguments but then expects that they contain
  either a :database-name or a :collection-name in their metadata and that is used to build the url string."
  [& parts]
    (let [url-string 
        (reduce 
          (fn [base-part add-part]
            ;; if parts are keywords, cast them into strings
            (let [base-string (if (keyword? base-part) (name base-part) base-part)
                  add-string (if (keyword? add-part) (name add-part) add-part)]
              (if (nil? add-string) base-string 
                (let [add-url-string (if (.startsWith (clojure.string/trim add-string) "/") (subs (clojure.string/trim add-string) 1) (clojure.string/trim add-string))
                      url-string (str base-string (clojure.string/trim add-url-string))]
                  ;; if already ends with '/' just add it, otherwise append '/'
                  ;; if it ends with '=' it's a parameter and also doesn't need a '/'
                  (if (or (.endsWith add-url-string "/") (.endsWith add-url-string "=")) url-string (str url-string "/"))))))
        "" ;the start of the url string is an empty string
        (map #(if (or (= (type %) clojure.lang.PersistentHashMap) (= (type %) clojure.lang.PersistentArrayMap)) (extract-db-or-collection-name %) %) parts))]
      ;; cut off the last '/'
      (.substring url-string 0 (dec (count url-string)))))

(defn build-resource-uri
  "Build a URI to access a resource in ArangoDB. Uses the defaults set in the clarango-connection 
  for the parts of the URI that are not specified by arguments.

  First argument must be the resource type as a string. E.g. 'document' 
  OR 'simple/first-example' if you want to access the first-example method in the simple namespace without 
  appending a collection name (in this case you should explicitely pass nil as collection-name)
  OR 'document/?collection=' if you need the collection name appended as a parameter."
  ([type]
    {:pre [(or (string? type) (nil? type))]}
    (connect-url-parts (get-safe-connection-url) "_api/" type))
  ([type resource-key]
    {:pre [(or (string? type) (nil? type)) (or (string? resource-key) (nil? resource-key) (keyword? resource-key))]}
    (connect-url-parts (get-safe-connection-url) "_db/" (get-default-db) "_api/" type (get-default-collection-or-graph type) resource-key))
  ([type resource-key collection-name]
    {:pre [(or (string? type) (nil? type)) (or (string? resource-key) (nil? resource-key) (keyword? resource-key))]}
  	(connect-url-parts (get-safe-connection-url) "_db/" (get-default-db) "_api/" type collection-name resource-key))
  ([type resource-key collection-name db-name]
    {:pre [(or (string? type) (nil? type)) (or (string? resource-key) (nil? resource-key) (keyword? resource-key))]}
  	(connect-url-parts (get-safe-connection-url) "_db/" db-name "_api/" type collection-name resource-key)))

(defn build-document-uri-from-two-parts
  "Builds a document uri from the part url that is returned by the collection/get-all-documents method and a db name.
  The db name can be nil, in which case the default database will be used.

  This method is meant to be used only by the collection/get-delayed-collection method."
  [document-uri db-name]
  {:pre [(string? document-uri) (or (string? db-name) (nil? db-name))]}
  (let [db-name-not-nil (if (nil? db-name) (get-default-db) db-name)]
    (connect-url-parts (get-safe-connection-url) "_db/" db-name-not-nil document-uri)))
