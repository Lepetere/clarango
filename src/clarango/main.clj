(ns clarango.main
	(:require [clarango.core :as clarango.core]
				[clarango.collection :as collection]
				[clarango.document :as document])
	(:use clojure.pprint))

(def conn-path "_api/document/persons/23478695")
;; why does the follwing not work? -> (def conn-path "_db/_system/_api/document/persons/23478695")

;; DEMO to call lein run and test it without the lib usage
(defn -main []
  (clarango.core/connect! {:connection-url  "http://localhost:8529/"})
  (let [result (document/get-by-key conn-path "001")]
      (println "test without db name")
      (pprint result))
  (clarango.core/connect! {:connection-url  "http://localhost:8529/"
  							:db-name "_system"})
  (let [result (document/get-by-key conn-path "002")]
      (println "test with db name")
      (pprint result)))

;; TODO next: 
;; - document get method should determine if there is a global object set, overload it
;; - put READ method to clarango.core?         - to utility ns? -> should not be visible from the outside
;; - create build-REST-URI function in core ns _ |
;; - only get document from the server, not the whole collection

;; Test adresses:
;; http://echo.jsontest.com/key/value/one/two
;; http://localhost:8529/_db/_system/_api/document/persons/16600192