(ns clarango.document
  (:require [clarango.core :as clarango.core]
            [clarango.core-utility :as core-utility]
            [clarango.collection-utility :as collection-utility]))

;;; public interface
(defn post
  "Creates a document. Takes a hash that represents the document."
  [database collection document-hash]
  nil)

(defn put
  "Updates a document with a given key value pair."
  [database collection key value]
  nil)

;; Can hide clojure.core/get
(defn get-by-key
  "Gets a document by its key. Returns either a hash that represents the document if it exists or nil if it doesn't."
  ;;[database collection document-id]
  [conn-path key]
  (let [conn-map (clarango.core/get-connection)
        db-path (if (contains? conn-map :db-name) (str "_db/" (:db-name conn-map) "/") (str ""))
        ;;db-path (str "_db/" (:db-name conn-map) "/")
        conn-adr (str (:connection-url conn-map) db-path conn-path)
        result-map (core-utility/read-uri conn-adr)]
      (get result-map key)))

(defn delete
  "Deletes a document by its id. Returns either a hash that represents the document if it existed or nil if it didn't."
  [database collection document-id]
  nil)

(defn update
  "Updates the document that matches the given key and value."
  [database collection key-to-find value-to-find key-to-add value-to-add]
  nil)

;; ?
(defn update-by-id
  ""
  [database collection]
  nil)