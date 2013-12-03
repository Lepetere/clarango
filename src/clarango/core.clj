(ns clarango.core
	(:require [clj-http.client :as http] ;; https://github.com/dakrone/clj-http
		        [cheshire.core :refer :all]) ;; https://github.com/dakrone/cheshire for custom coding
	(:require [clarango.collection :as collection]
				[clarango.document :as document]
				[clarango.test :as test])
	(:use clojure.pprint))

(defn -main []
	(test/run-simple-test)
	)

(defn connect!
  "connects permanently to an ArangoDB host by setting a global variable"
  [url]
  nil)

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