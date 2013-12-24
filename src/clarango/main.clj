(ns clarango.main
	(:require [clarango.core :as clarango.core]
				[clarango.collection :as collection]
				[clarango.document :as document])
	(:use clojure.pprint))

;; DEMO to call lein run and test it without the lib usage
(defn -main []
  (let [key "46940583"]
    (println "test 0: get key " + key)
    (clarango.core/set-connection!)
    (let [result (document/get-by-key key "persons")]
      (pprint result)))

  (let [key "23478695"]
    (println "test 1: get key " + key)
    (clarango.core/set-connection! {:connection-url "http://localhost:8529/"})
    (let [result (document/get-by-key key "persons" "_system")]
      (pprint result)))
  (let [key "42025383"]
    (println "test 2: get key " + key)
    (clarango.core/set-connection! 
      {
        :connection-url "http://localhost:8529/"
        :db-name "_system"
        :collection-name "persons"
      })
    (let [result (clarango.document/get-by-key key)]
          (pprint result)))
  (let [key "42484135"]
    (println "test 3: get key " + key)
    (clarango.core/set-connection! {:connection-url "http://localhost:8529/"})
    (clarango.core/set-default-db! "_system")
    (let [result (document/get-by-key key "persons")]
            (pprint result))))