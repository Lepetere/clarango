(ns clarango.main
	(:require [clarango.core :as clarango.core]
      [clarango.utilities.core-utility :as core-utility]
				[clarango.collection :as collection]
				[clarango.document :as document]
        [clarango.database :as database]
        [clarango.query :as query]
        [clarango.graph :as graph])
	(:use clojure.pprint)
  (:use clarango.collection-ops)
  (:use clarango.core))

;; DEMO to call lein run and test it without the lib usage
(defn -main []

  ;;;; comprehensive usage example

  ;; connect to localhost
  (clarango.core/set-connection!)
  ;; create Database "test-DB"
  (pprint (database/create "test-DB" [{:username "test-user"}]))
  ;; create Collection "test-collection" in DB "test-DB"
  (pprint (collection/create "test-collection" "test-DB"))
  ;; document CRUD -> create :name "some test document" :additional "some additional info" :name "new version of test document"
  (pprint (document/create {:_key "test-doc" :name "some test document"} "test-collection" "test-DB"))
  (pprint (document/get-by-key "test-doc" "test-collection" "test-DB"))
  ;; set-db "test-DB"
  ;; collection ops : assoc, dissoc, conj
  ;; list all documents

  ;; create another Database "GraphTestDB"
  ;; list all databases
  ;; with-db "test-DB"
  ;; create vertex and edge collections "people", "connections"
  ;; list all collections
  ;; create graph
  ;; get all graphs
  ;; with-graph
  ;; create vertices "Peter", "Bob", "Clara", "Jessica", "Alice" with :ages
  ;; perform query: find all persons who are older than 25
  ;; create edges with labels "friend", "boyfriend", "girlfriend"
  ;; get vertices
  ;; update edge :description "Peter and Alice have been friends for over 6 years."
  ;; get edges
  ;; execute traversal
  ;; delete one vertex
  ;; delete one edge
  ;; delete collections
  ;; delete "GraphTestDB"
  )
