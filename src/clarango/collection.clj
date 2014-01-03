(ns clarango.collection
  (:require [clarango.core :as clarango.core]))

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

(defn get
  "Loads a collection and returns a reference to it."
  [database collection-name]
  nil)

(defn get-all-documents ; in the ArangoDB REST API this method is part of the Document API, but is this here not a better place?
  "Returns all documents of a collection."
  [database collection document-id]
  nil)