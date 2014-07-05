;; Please note:
;; The methods in this namespace are only intended for internal use.

(ns clarango.utilities.uri-utility
	(:use [clarango.utilities.core-utility :only 
    [get-safe-connection-url get-default-db get-default-collection-name get-default-graph-name]]))

(defn get-default-collection-or-graph
  "In case the type parameter is 'graph', returns the default graph that was set in core.
  In all other cases returns the default collection."
  [type]
  (if (= type "graph") (get-default-graph-name) (get-default-collection-name)))

(defn connect-url-parts
  "Builds a string out of different parts. Adds a '/' between the string parts if not present."
  [& parts]
    (let [url-string 
        (reduce 
          (fn [base-string add-string]
            (if (nil? add-string) base-string 
              (let [url-string (str base-string (clojure.string/trim add-string))]
                ;; if already ends with '/' just add it, otherwise append '/'
                ;; if it ends with '=' it's a parameter and also doesn't need a '/'
                (if (or (.endsWith add-string "/") (.endsWith add-string "=")) url-string (str url-string "/")))))
        "" parts)]
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
    {:pre [(or (string? type) (nil? type)) (or (string? resource-key) (nil? resource-key))]}
  	(connect-url-parts (get-safe-connection-url) "_db/" (get-default-db) "_api/" type (get-default-collection-or-graph type) resource-key))
  ([type resource-key collection-name]
    {:pre [(or (string? type) (nil? type)) (or (string? resource-key) (nil? resource-key)) (or (string? collection-name) (nil? collection-name))]}
  	(connect-url-parts (get-safe-connection-url) "_db/" (get-default-db) "_api/" type collection-name resource-key))
  ([type resource-key collection-name db-name]
    {:pre [(or (string? type) (nil? type)) (or (string? resource-key) (nil? resource-key)) (or (string? collection-name) (nil? collection-name)) (or (string? db-name) (nil? db-name))]}
  	(connect-url-parts (get-safe-connection-url) "_db/" db-name "_api/" type collection-name resource-key)))