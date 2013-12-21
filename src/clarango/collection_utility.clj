(ns clarango.collection-utility
  (:require [clarango.core :as clarango.core]))

(defn ^:private get-connection-url
	"Gets the globally in clarango.core set connection url. If no url is set, an exception is thrown."
	[]
	(if (clarango.core/connection-set?) 
		(:connection-url (clarango.core/get-connection)) 
		(throw (Exception. "No host URL set."))))

(defn ^:private get-safe-connection-url
	"Gets a safe connection url from clarango.core. If it does not start with 'http://', an exception is thrown."
	[]
	(let [conn-url (get-connection-url)]
		(if (.startsWith conn-url "http://") 
			conn-url 
			(throw (Exception. "Somethings wrong with the URL. Does not start with http.")))))

(defn ^:private get-default-db
	"Gets the globally in clarango.core set db name. If none is set, an exception is thrown."
	[]
	(if (clarango.core/connection-set?)
		(let [db-name (:db-name (clarango.core/get-connection))]
			(if (nil? db-name) (throw (Exception. "No default database set.")) db-name))
		(throw (Exception. "No connection set."))))

(defn ^:private get-default-collection-id
	"Gets the globally in clarango.core set collection id. If none is set, an exception is thrown."
	[]
	(if (clarango.core/connection-set?) 
		(let [collection-id (:collection-name (clarango.core/get-connection))]
			(if (nil? collection-id) (throw (Exception. "No default collection set.")) collection-id)) 
		(throw (Exception. "No connection set."))))

(defn ends-with
	"Tests if a string ends with a given character (string of length 1)."
	[string-to-test character]
  (= (dec (count string-to-test)) (.indexOf string-to-test character (dec (count string-to-test)))))

(defn ^:private connect-url-parts
  "Builds a string out of different parts. Adds a '/' at the end of each string part if not present."
  [& parts]
    (reduce 
      (fn [base-string add-string]
        (let [url-string (str base-string (clojure.string/trim add-string))] 
          (if (.endsWith add-string "/") url-string (str url-string "/"))))
            "" parts))

(defn build-collection-URI
  "Build a URI to access a collection in ArangoDB. Uses the defaults set in the clarango-connection 
  for the parts of the URI that are not specified by arguments."
  ;; this is what a URI should look like coming out of this method:
  ;; http://localhost:8529/_db/_system/_api/document/persons/23478695
  ([]
  	(connect-url-parts (get-safe-connection-url) "_db/" (get-default-db) "_api/document/" (get-default-collection-id)))
  ([collection-id]
  	(connect-url-parts (get-safe-connection-url) "_db/" (get-default-db) "_api/document/" collection-id))
  ([db-name collection-id]
  	(connect-url-parts (get-safe-connection-url) "_db/" db-name "_api/document/" collection-id)))
