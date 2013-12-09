(ns clarango.test
	(:require [clj-http.client :as http] ;; https://github.com/dakrone/clj-http
		        [cheshire.core :refer :all]) ;; https://github.com/dakrone/cheshire for custom coding
	(:require [clarango.core :as clarango.core])
	(:use clojure.pprint))

(declare READ) ;; just for testing with main, lib does not need this later

;; maybe a universal defrecord is better like neocons does
;;; (def conn-adr "http://localhost:8529/_db/_system/_api/document/persons/16600192")
(def conn-path "_api/document/persons/23478695")

(defn READ [url] ;; define timeout?
	(parse-string (:body (http/get url))))

;; --------------------------------------------
;; DEMO to call lein run and test it without the lib usage
(defn run-simple-test []
	(let [	conn-adr (str (clarango.core/get-connection-url) conn-path)
			result (READ conn-adr)]
		(println "connection address: " conn-adr)
		(println "\nRAW:")
		(pprint result)
		(println "\nSTRIPPED:")
		(pprint  (get result "002"))) ;; strip off the rest, select key to get the real content
	
	;; suggestion for connection interface
	#_(
	(let [connection (clarango.core/connect-host "http://localhost:8529/")]
		(clarango.document/get connection "db_name" "collection_name" "document_id"))

	(let [connection (clarango.core/connect-db "http://localhost:8529/" "db_name")]
		(clarango.document/get connection "collection_name" "document_id"))

	(let [connection (clarango.core/connect-collection "http://localhost:8529/" "db_name" "collection_name")]
		(clarango.document/get connection "document_id"))

	;; would be even cooler if the following also works:
	(let [connection-host (clarango.core/connect-host "http://localhost:8529/")
			connection-db (clarango.core/connect-db connection-host "db_name")
			connection-collection (clarango.core/connect-collection connection-db "collection_name")]
		(clarango.document/get connection-collection "document_id"))

	;; even more connection methods to create a new db and collection
	(let [connection (clarango.core/connect-new-db "http://localhost:8529/" "db_name")]
		(clarango.collection/create connection "collection_name"))

	(let [connection-db (clarango.core/connect-db "http://localhost:8529/" "db_name")
			connection-collection (clarango.core/connect-new-collection connection-db "new_collection_name")] ;; problem here is: clarango.core/connect-new-collection cannot call clarango.collection/create due to dependency direction; that means we would have kind of a create collection method in both namespaces...
		(clarango.document/post connection-collection {name: "Peter"}))))

;; Test adresses:
;; http://echo.jsontest.com/key/value/one/two
;; http://localhost:8529/_db/_system/_api/document/persons/16600192

;; {"001" {"age" 42, "name" "edlich"}, "_id" "persons/16600192", "_rev" "18238592",  "_key" "16600192"}