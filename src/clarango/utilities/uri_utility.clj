;; Please note:
;; The methods in this namespace are only intended for internal use.

(ns clarango.utilities.uri-utility
	(:use [clarango.utilities.core-utility :only [get-safe-connection-url get-default-db get-default-collection-name]]))

(defn connect-url-parts
  "Builds a string out of different parts. Adds a '/' at the end of each string part if not present."
  [& parts]
    (reduce 
      (fn [base-string add-string]
        (let [url-string (str base-string (clojure.string/trim add-string))] 
          (if (.endsWith add-string "/") url-string (str url-string "/"))))
            "" parts))

(defn build-uri
  "Build a URI to access a ressource in ArangoDB. Uses the defaults set in the clarango-connection 
  for the parts of the URI that are not specified by arguments.

  First argument must be the ressource type as a string. E.g. 'document'.
  If nil is passed as ressource-key and collection-name, a generic endpoint like <host><db>_api/simple is returned."
  ([type ressource-key]
  	(connect-url-parts (get-safe-connection-url) "_db/" (get-default-db) "_api/" type (get-default-collection-name) ressource-key))
  ([type ressource-key collection-name]
  	(connect-url-parts (get-safe-connection-url) "_db/" (get-default-db) "_api/" type collection-name ressource-key))
  ([type ressource-key collection-name db-name]
  	(connect-url-parts (get-safe-connection-url) "_db/" db-name "_api/" type collection-name ressource-key)))

(defn build-document-base-url
  "Builds the base url for the document endpoint including a collection name as parameter. Can be used to post documents.
  Uses the defaults set in the clarango-connection for the parts of the URL that are not specified by arguments."
  ([]
  	(str (connect-url-parts (get-safe-connection-url) "_db/" (get-default-db)) "_api/document?collection=" (get-default-collection-name)))
  ([collection-name]
  	(str (connect-url-parts (get-safe-connection-url) "_db/" (get-default-db)) "_api/document?collection=" collection-name))
  ([collection-name db-name]
  	(str (connect-url-parts (get-safe-connection-url) "_db/" db-name) "_api/document?collection=" collection-name)))