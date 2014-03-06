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


(defn -main []

  ;;;; this is a comprehensive usage example that tries to give an overview over all the features of the Clarango API
  ;;;; it tries to make use of all methods available in Clarango, but sometimes methods are left out if they are 
  ;;;; very similar to methods already used

  ;;; first create a database and a collection and make some document CRUD

  ;; connect to defaults: localhost and port 8529
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
  

  ;;; now make use of the clojure idiomatic methods available in the namespace collection-ops 
  ;;; to add and delete more content in the collection

  ;; set default DB; this database will be used in the following methods without explicitely having to pass it
  (cla-core/set-default-db! "test-DB")
  ;; collection ops : assoc, dissoc, conj
  (pprint (cla-assoc! "test-collection" "new-document-1" {:description "some test document to test the clojure idiomatic collection methods" :key-type "given key"}))
  (pprint (cla-conj! "test-collection" {:description "some test document to test the clojure idiomatic collection methods" :key-type "auto generated key"}))
  (pprint (cla-get! "test-collection" "new-document-1"))
  (pprint (cla-dissoc! "test-collection" "new-document-1"))


  ;;; modify the collection

  ;; get information about the collection and a list of all documents inside it
  (pprint (collection/get-info "test-collection"))
  (pprint (collection/get-all-documents "test-collection"))
  ;; rename the collection and modify it's properties
  (pprint (collection/rename "new-name-test-collection" "test-collection"))
  (pprint (collection/modify-properties {"waitForSync" true} "new-name-test-collection"))
  (pprint (collection/get-extended-info-figures "new-name-test-collection"))
  ;; unload and delete collection
  (pprint (collection/unload "new-name-test-collection"))
  (pprint (collection/delete "new-name-test-collection"))


  ;;; now create a graph, perform some graph operations and query it's vertices

  ;; first create another Database "GraphTestDB"
  (database/create "GraphTestDB" [])
  ;; now list all available databases
  (pprint (database/get-info-list))
  ;; perform next operations in the context of "GraphTestDB"
  (with-db "GraphTestDB"
    ;; create vertex and edge collections "people" and "connections"
    (collection/create "people" {"type" 2})
    (collection/create "connections" {"type" 3})
    ;; now list all available collections, excluding the system collections
    (pprint (database/get-collection-info-list {"excludeSystem" true}))
    ;; create graph "test-graph"
    (pprint (graph/create "test-graph" "people" "connections"))
    ;; now get all available graphs
    (pprint (database/get-all-graphs))
    ;; perform next operations in the context of the graph "test-graph"
    (with-graph "test-graph"
      ;; create vertices "Peter", "Bob", "Clara", "Jessica", "Alice" with :ages
      (graph/create-vertex {:_key "peter" :name "Peter" :age 25})
      (graph/create-vertex {:_key "bob" :name "Bob" :age 28})
      (graph/create-vertex {:_key "clara" :name "Clara" :age 29})
      (graph/create-vertex {:_key "jessica" :name "Jessica" :age 23})
      (graph/create-vertex {:_key "alice" :name "Alice" :age 20})

      ;;; perform query: find all people who are older than 24
      ;; first validate the query, then explain (how the query would be executed on the server), then actually execute it
      (pprint (query/validate "FOR p IN people FILTER p.age > 24 RETURN p"))
      (pprint (query/explain "FOR p IN people FILTER p.age > 24 RETURN p"))
      (pprint (query/execute "FOR p IN people FILTER p.age > 24 RETURN p"))

      ;; create edges with labels "friend", "boyfriend", "girlfriend"; save one key to use this edge later
      (let [edge-key (get (graph/create-edge {:$label "friend"} "peter" "alice") "_key")]
        (graph/create-edge {:$label "friend"} "alice" "clara")
        (graph/create-edge {:$label "friend"} "clara" "jessica")
        (graph/create-edge {:$label "boyfriend"} "alice" "bob")
        (graph/create-edge {:$label "girlfriend"} "bob" "alice")
        ;; get vertices that have connections going from the vertex "peter"
        (pprint (graph/get-vertices "peter" 10 10 true nil))
        ;; update one edge
        (pprint (graph/update-edge {:description "Peter and Alice have been friends for over 6 years"} edge-key))
        ;; get all edges that are outgoing from the vertex "peter"
        (pprint (graph/get-edges "peter" 10 10 true nil))
        ;; execute a graph traversal
        (pprint (graph/execute-traversal "peter" "people" "connections" "inbound"))
        ;; delete one vertex
        (pprint (graph/delete-vertex "peter"))
        ;; delete one edge
        #_(pprint (graph/delete-edge edge-key))))
    ;; delete the graph
    (pprint (graph/delete "test-graph")))
  
  ;; delete databases
  (pprint (database/delete "GraphTestDB"))
  (database/delete "test-DB"))
