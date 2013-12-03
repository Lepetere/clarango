(ns clarango.core
	(:require [clj-http.client :as http] ;; https://github.com/dakrone/clj-http
		        [cheshire.core :refer :all]) ;; https://github.com/dakrone/cheshire for custom coding
	(:use clojure.pprint))

(declare READ) ;; just for testing with main, lib does not need this later

;; maybe a universal defrecord is better like neocons does
;;; (def conn-adr "http://localhost:8529/_db/_system/_api/document/persons/16600192")
(def conn-adr "http://localhost:8529/_api/document/persons/23478695")

(defn READ [url] ;; define timeout?
	(parse-string (:body (http/get url))))

;; --------------------------------------------
;; DEMO to call lein run and test it without the lib usage
(defn -main []
	(let [result (READ conn-adr)]
		(println "RAW:")
		(pprint result)
		(println "\nSTRIPPED:")
		(pprint  (get result "002")))) ;; strip off the rest, select key to get the real content

;; Test adresses:
;; http://echo.jsontest.com/key/value/one/two
;; http://localhost:8529/_db/_system/_api/document/persons/16600192

;; {"001" {"age" 42, "name" "edlich"}, "_id" "persons/16600192", "_rev" "18238592",  "_key" "16600192"}