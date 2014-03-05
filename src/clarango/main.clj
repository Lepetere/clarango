(ns clarango.main
	(:require [clarango.core :as cla-core]
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
  (pprint (cla-assoc! "test-collection" "new-document-1" {:description "some test document to test the clojure idiomatic collection methods" :key-type "given key"}))
  (pprint (cla-conj! "test-collection" {:description "some test document to test the clojure idiomatic collection methods" :key-type "auto generated key"}))
  (pprint (cla-get! "test-collection" "new-document-1"))
  (pprint (cla-dissoc! "test-collection" "new-document-1"))
  ;; get collection properties, modify properties, rename collection
  (pprint (collection/get-all-documents "test-collection"))
  (pprint (collection/get-info "test-collection"))
  (pprint (collection/rename "new-name-test-collection" "test-collection"))
  (pprint (collection/modify-properties {"waitForSync" true} "new-name-test-collection"))
  (pprint (collection/get-extended-info-figures "new-name-test-collection"))
  ;; unload and delete collection
  (pprint (collection/unload "new-name-test-collection"))
  (pprint (collection/delete "new-name-test-collection"))

  ;; create another Database "GraphTestDB"
  (database/create "GraphTestDB" [])
  ;; list all databases
  (pprint (database/get-info-list))
  ;; with-db "test-DB"
  (with-db "GraphTestDB"
    ;; create vertex and edge collections "people", "connections"
    (collection/create "people" {"type" 2})
    (collection/create "connections" {"type" 3})
    ;; list all collections
    (pprint (database/get-collection-info-list {"excludeSystem" true}))
    ;; create graph
    (pprint (graph/create "test-graph" "people" "connections"))
    ;; get all graphs
    (pprint (database/get-all-graphs))
    ;; with-graph
    (with-graph "test-graph"
      ;; create vertices "Peter", "Bob", "Clara", "Jessica", "Alice" with :ages; save one key
      (let [vertex-key (:_key (graph/create-vertex {:name "Peter" :age 25}))]
        (graph/create-vertex {:name "Bob" :age 28})
        (graph/create-vertex {:name "Clara" :age 29})
        (graph/create-vertex {:name "Jessica" :age 23})
        (graph/create-vertex {:name "Alice" :age 20})
        ;; perform query: find all persons who are older than 25
        ;; create edges with labels "friend", "boyfriend", "girlfriend"; save one key
        (let [edge-key (graph/create-edge {:$label "friend"} )]
        ;; get vertices
        (pprint (graph/get-vertices vertex-key 10 10 true nil))
        ;; update edge :description "Peter and Alice have been friends for over 6 years."
        ;; get edges
        (pprint (graph/get-edges edge-key 10 10 true nil))))
      ;; execute traversal
      ;; delete one vertex
      ;; delete one edge
    )
    (pprint (graph/delete "test-graph"))
  )
  ;; delete "GraphTestDB"
  (pprint (database/delete "GraphTestDB"))
  (database/delete "test-DB"))
