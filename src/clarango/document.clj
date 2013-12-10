(ns clarango.document
	(:require [clj-http.client :as http] ;; https://github.com/dakrone/clj-http
		        [cheshire.core :refer :all]) ;; https://github.com/dakrone/cheshire for custom coding
  (:require [clarango.core :as clarango.core])
	(:use clojure.pprint))

;;; utilities
(defn READ [url] ;; define timeout?
  (println "connection address: " url)
  (parse-string (:body (http/get url))))

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
(defn get-key
  "Gets a document by its id. Returns either a hash that represents the document if it exists or nil if it doesn't."
  ;;[database collection document-id]
  [conn-path key]
  (let [conn-adr (str (:connection-url (clarango.core/get-connection)) conn-path)
        result-map (READ conn-adr)]
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