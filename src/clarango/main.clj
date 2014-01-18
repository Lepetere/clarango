(ns clarango.main
	(:require [clarango.core :as clarango.core]
      [clarango.utilities.core-utility :as core-utility]
				[clarango.collection :as collection]
				[clarango.document :as document]
        [clarango.database :as database])
	(:use clojure.pprint))

;; DEMO to call lein run and test it without the lib usage
(defn -main []

  (println "\n\ntest different connection settings:\n")
  ;;; test different types of get-by-key parameter combinations
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
  #_(let [key "errorTest" ; will evoke an error if uncommented
        result (clarango.document/get-by-key key "persons" "_system")]
          (pprint result))
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
    (println "test 3: get and head key " + key)
    (clarango.core/set-connection! {:connection-url "http://localhost:8529/"})
    (clarango.core/set-default-db! "_system")
    (let [result (document/get-by-key key "persons")]
            (pprint result))
    (let [result (document/get-info key "persons")]
            (pprint result)))

  (println "\n\ntest document CRUD:\n")
  ;;; test post/put/patch/delete
  (let [document {:name "toller Name" :city "wo kommt er her?"}]
    (println "test 4: post document")
    (let [result-doc (document/create document "persons" "_system")
          new-key (get result-doc "_key")
          rev (get result-doc "_rev")]
            (pprint result-doc)
            (let [document-new {:name "noch besserer Name" :city "aus Augsburg natürlich"}]
              (println "test 5: put document")
              (let [result (document/replace-by-key document-new new-key "persons" "_system")]
              (pprint result)))
            (let [document-update {:alter "Er ist schon 100 Jahre alt."}]
              (println "test 6: patch document")
              (let [result (document/update-by-key document-update new-key "persons" "_system")]
              (pprint result)))
            (let [result (document/get-first-by-example {:name "noch besserer Name"} "persons" "_system")]
              (println "test 7: get current document state by example")
              (pprint result))
            (println "test 8: delete document")
            (let [result (document/delete-by-key new-key {"rev" "100000" "policy" "last"} "persons" "_system")]
              (pprint result))))

  ;;; test collection methods
  (println "\n\ntest collection methods:\n")
  (clarango.core/set-connection! 
    {
      :connection-url "http://localhost:8529/"
      :db-name "_system"
      :collection-name "persons"
    })
  (let [result (clarango.collection/load {"count" false})]
        (pprint result))
  (let [result (clarango.collection/get-extended-info-figures)]
        (pprint result))
  #_(let [result (clarango.collection/unload)]
        (pprint result))

  ;;; test database methods
  (println "\n\ntest database methods:\n")
  (clarango.core/set-connection!)
  (pprint (clarango.database/get-info-current))
  (pprint (clarango.database/get-info-list))
  (pprint (clarango.database/get-info-user)))
