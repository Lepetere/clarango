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
  (clarango.core/set-default-db! "_system")
  (let [result (document/get-by-key conn-path "002")]
      (println "test with db name")
      (pprint result)))