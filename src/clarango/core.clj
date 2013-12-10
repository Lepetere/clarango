(ns clarango.core
	(:require [clj-http.client :as http] ;; https://github.com/dakrone/clj-http
		        [cheshire.core :refer :all]) ;; https://github.com/dakrone/cheshire for custom coding
	(:use clojure.pprint))

(declare clarango-connection)

(defn connect!
  "Connects permanently to an ArangoDB host by setting the connection map as a global variable."
  [connection-map]
  ;; TODO: test if url is string and starts with http://
  ;; TODO: test if there is a database server available
  (def clarango-connection connection-map)
  )

(defn get-connection
  "Returns the db server connection map to other namespaces."
  []
  clarango-connection
  ;; TODO: throw exception if no server url is set?
  )

(defn create-database
  "Creates a database with the given name."
  [database-name]
  nil)

(defn get-database
  "Returns a reference to a given database."
  [database-name]
  nil)