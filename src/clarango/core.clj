(ns clarango.core
	(:require [clj-http.client :as http] ;; https://github.com/dakrone/clj-http
		        [cheshire.core :refer :all]) ;; https://github.com/dakrone/cheshire for custom coding
	(:use clojure.pprint))

(declare clarango-connection)

(defn connect!
  "connects permanently to an ArangoDB host by setting a global variable"
  [url]
  ;; TODO: test if url is string and starts with http://
  ;; TODO: test if there is a database server available
  (def clarango-connection url)
  )

(defn get-connection-url
  "returns the db server connection url to other namespaces"
  []
  clarango-connection
  ;; TODO: throw exception if no server url is set
  )

(defn connect
  "connects to an ArangoDB host by returning a reference to it"
  [url]
  nil)

(defn create-database
  "creates a database with the given name"
  [database-name]
  nil)

(defn get-database
  "returns a reference to a given database"
  [database-name]
  nil)