(ns clarango.main
	(:require [clarango.core :as cla-core]
				[clarango.collection :as collection]
				[clarango.document :as document]
        [clarango.database :as database]
        [clarango.query :as query]
        [clarango.graph :as graph]
        [clarango.admin :as admin])
	(:use clojure.pprint)
  (:use clarango.collection-ops)
  (:use clarango.core))


(defn -main []

  ;;;; this is a comprehensive usage example that tries to give an overview over all the features of the Clarango API
  ;;;; it tries to make use of all methods available in Clarango, but sometimes methods are left out if they are 
  ;;;; very similar to methods already used

  (println "\n\n---- first create a database and a collection and make some document CRUD ----\n")

  (println "\nconnect to defaults: localhost and port 8529")
  (cla-core/set-connection!)
  (println "\ncreate Database 'test-DB'")
  (pprint (database/create "test-DB" [{:username "test-user"}]))
  (println "\ncreate Collection 'test-collection' in DB 'test-DB'")
  (pprint (collection/create "test-collection" "test-DB"))
  (println "\ndocument CRUD")
  (pprint (document/create-with-key {:name "some test document"} "test-doc" "test-collection" "test-DB"))
  (pprint (document/update-by-key {:additional "some additional info"} "test-doc" "test-collection" "test-DB"))
  (pprint (document/get-by-key "test-doc" "test-collection" "test-DB"))
  (pprint (document/replace-by-example {:name "new version of our test document"} {:additional "some additional info"} "test-collection" "test-DB"))
  

  (println "\n\n---- now make use of the clojure idiomatic methods available in the namespace collection-ops to add and delete more content in the collection ----\n")

  (println "\nset default DB; this database will be used in the following methods without explicitely having to pass it")
  (cla-core/set-default-db! "test-DB")
  (println "\ncollection ops : assoc, dissoc, conj")
  (pprint (cla-assoc! "test-collection" "new-document-1" {:description "some test document to test the clojure idiomatic collection methods" :key-type "given key"}))
  (pprint (cla-conj! "test-collection" {:description "some test document to test the clojure idiomatic collection methods" :key-type "auto generated key"}))
  (pprint (cla-get! "test-collection" "new-document-1"))
  (pprint (cla-dissoc! "test-collection" "new-document-1"))


  (println "\n\n---- modify the collection ----\n")

  (println "\nget information about the collection and a list of all documents inside it")
  (pprint (collection/get-info "test-collection"))
  (pprint (collection/get-all-documents "test-collection"))
  (println "\nrename the collection and modify it's properties")
  (pprint (collection/rename "new-name-test-collection" "test-collection"))
  (pprint (collection/modify-properties {"waitForSync" true} "new-name-test-collection"))
  (pprint (collection/get-extended-info-figures "new-name-test-collection"))
  (println "\nunload and delete collection")
  (pprint (collection/unload "new-name-test-collection"))
  (pprint (collection/delete "new-name-test-collection"))


  (println "\n\n---- now create a graph, query it's vertices and perform some graph operations including a traversal ----\n")

  (println "\nfirst create another Database 'GraphTestDB'")
  (database/create "GraphTestDB" [])
  (println "\nnow list all available databases")
  (pprint (database/get-info-list))
  (println "\nperform next operations in the context of 'GraphTestDB'")
  (with-db "GraphTestDB"
    (println "\ncreate vertex and edge collections 'people' and 'connections'")
    (collection/create "people" {"type" 2})
    (collection/create "connections" {"type" 3})
    (println "\nnow list all available collections, excluding the system collections")
    (pprint (database/get-collection-info-list {"excludeSystem" true}))
    (println "\ncreate graph 'test-graph'")
    (pprint (graph/create "test-graph" "people" "connections"))
    (println "\nnow get all available graphs")
    (pprint (database/get-all-graphs))
    (println "\nperform next operations in the context of the graph 'test-graph'")
    (with-graph "test-graph"
      (println "\ncreate vertices 'Peter', 'Bob', 'Clara', 'Jessica', 'Alice' with :ages")
      (graph/create-vertex {:_key "peter" :name "Peter" :age 25})
      (graph/create-vertex {:_key "bob" :name "Bob" :age 28})
      (graph/create-vertex {:_key "clara" :name "Clara" :age 29})
      (graph/create-vertex {:_key "jessica" :name "Jessica" :age 23})
      (graph/create-vertex {:_key "alice" :name "Alice" :age 20})

      (println "\n\n---- perform query: find all people who are older than 24\nfirst validate the query, then explain (how the query would be executed on the server), then actually execute it ----\n")
      (pprint (query/validate "FOR p IN people FILTER p.age > 24 RETURN p"))
      (pprint (query/explain "FOR p IN people FILTER p.age > 24 RETURN p"))
      (pprint (query/execute "FOR p IN people FILTER p.age > 24 RETURN p"))

      (println "\ncreate edges with labels 'friend', 'boyfriend', 'girlfriend'; save one key to use this edge later")
      (let [edge-key (get (graph/create-edge {:$label "friend"} "peter" "alice") "_key")]
        (graph/create-edge {:$label "friend"} "alice" "clara")
        (graph/create-edge {:$label "friend"} "clara" "jessica")
        (graph/create-edge {:$label "boyfriend"} "alice" "bob")
        (graph/create-edge {:$label "girlfriend"} "bob" "alice")
        (println "\nget vertices that have connections going from the vertex 'peter'")
        (pprint (graph/execute-vertex-traversal "peter" 10 10 true nil))
        (println "\nupdate one edge")
        (pprint (graph/update-edge {:description "Peter and Alice have been friends for over 6 years"} edge-key))
        (println "\nget all edges that are outgoing from the vertex 'peter'")
        (pprint (graph/execute-edge-traversal "peter" 10 10 true nil))
        (println "\nexecute a graph traversal")
        (pprint (graph/execute-traversal "peter" "people" "connections" "inbound"))
        (println "\ndelete one edge")
        (pprint (graph/delete-edge edge-key))
        (println "\ndelete one vertex")
        (pprint (graph/delete-vertex "peter"))))
    (println "\ndelete the graph")
    (pprint (graph/delete "test-graph")))

  (println "\nFlush:") ;; all admin functions now
  (pprint (admin/flush))
  (println "\nReload:")
  (pprint (admin/reload))
  (println "\nStatistics:")
  (pprint (admin/statistics))
  (println "\nStatistic descriptions:")
  (pprint (admin/stat-desc))
  (println "\nLog")
  (pprint (admin/log {"upto" 4}))
  (println "\nRole:") ; This is >= V2
  (pprint (admin/role))
  
  (println "\ndelete databases")
  (pprint (database/delete "GraphTestDB"))
  (database/delete "test-DB"))
