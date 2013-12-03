(ns clarango.collection
	(:require [clj-http.client :as http] ;; https://github.com/dakrone/clj-http
		        [cheshire.core :refer :all]) ;; https://github.com/dakrone/cheshire for custom coding
	(:use clojure.pprint))

(defn create
  "Creates a collection and returns a reference to it. Returns nil if the collection already exists."
  [database collection-name]
  nil)

;; Can hide clojure.core/drop
(defn drop
  "Drops a collection."
  [database collection-name]
  nil)

;; Hides clojure.set/rename -> Find a different name?
(defn rename
  "Renames a collection."
  [database collection-name collection-new-name]
  nil)

;; Name too verbose? "get" would get confused with the document/get method. "load" is also not very good because it would suggest a different behaviour than the core/get-database method, although it does the same, just for collections.
(defn get-collection
  "Loads a collection and returns a reference to it."
  [database collection-name]
  nil)

;;; Maybe make it possible in all these methods to pass either a reference to the database or just the database name?