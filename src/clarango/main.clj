(ns clarango.main
	(:require [clarango.core :as cla-core]
      [clarango.utilities.core-utility :as core-utility]
				[clarango.collection :as collection]
				[clarango.document :as document]
        [clarango.database :as database]
        [clarango.query :as query]
        [clarango.graph :as graph])
	(:use clojure.pprint)
  (:use clarango.collection-ops))

;; DEMO to call lein run and test it without the lib usage
(defn -main []

  ;;;; comprehensive usage example

  ;; connect to localhost
  (cla-core/set-connection!)
  ;; create Database "test-DB"
  (pprint (database/create "test-DB" [{:username "test-user"}]))
  ;; create Collection "test-collection" in DB "test-DB"
  (pprint (collection/create "test-collection" "test-DB"))
  ;; document CRUD
  (pprint (document/create {:_key "test-doc" :name "some test document"} "test-collection" "test-DB"))
  (pprint (document/update-by-key {:additional "some additional info"} "test-doc" "test-collection" "test-DB"))
  (pprint (document/get-by-key "test-doc" "test-collection" "test-DB"))
  (pprint (document/replace-by-example {:name "new version of our test document"} {:additional "some additional info"} "test-collection" "test-DB"))
  ;; set-db "test-DB"
  (cla-core/set-default-db! "test-DB")
  ;; collection ops : assoc, dissoc, conj
  ;; get collection properties, modify properties, rename collection
  (pprint (collection/get-info "test-collection"))
  (pprint (collection/get-all-documents "test-collection"))
  (pprint (collection/rename "new-name-test-collection" "test-collection"))
  (pprint (collection/modify-properties {"waitForSync" true} "new-name-test-collection"))
  (pprint (collection/get-extended-info-figures "new-name-test-collection"))
  ;; unload and delete collection
  (pprint (collection/unload "new-name-test-collection"))
  (pprint (collection/delete "new-name-test-collection"))

  ;; create another Database "GraphTestDB"
  (database/create "GraphTestDB" [])
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
  ;; delete "GraphTestDB"
  (pprint (database/delete "GraphTestDB"))
  (database/delete "test-DB"))
