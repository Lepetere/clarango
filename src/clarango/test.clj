(ns clarango.test
	(:require [clarango.document :as document])
	(:use clojure.pprint))

;; maybe a universal defrecord is better like neocons does
;;; (def conn-adr "http://localhost:8529/_db/_system/_api/document/persons/16600192")
(def conn-path "_api/document/persons/23478695")

;; --------------------------------------------
;; DEMO to call lein run and test it without the lib usage
(defn run-simple-test []
	
	(let [result (document/get conn-path)]
		(println "RAW:")
		(pprint result)
		(println "\nSTRIPPED:")
		(pprint  (get result "002")))
	)

;; TODO next: 
;; - document get method should determine if there is a global object set
;; - get method should convert json to clojure map
;; - put READ method to clarango.core?

;; Test adresses:
;; http://echo.jsontest.com/key/value/one/two
;; http://localhost:8529/_db/_system/_api/document/persons/16600192