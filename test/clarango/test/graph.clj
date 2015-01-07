(ns clarango.test.graph
  (:require [clojure.test :refer :all]
            [clarango.core :as cla-core]
            [clarango.collection :as collection]
            [clarango.database :as database]
            [clarango.query :as query]
            [clarango.graph :as graph]
            [clarango.misc :as misc])
  (:use clojure.pprint)
  (:use clarango.collection-ops)
  (:use clarango.core))

(defn setup []
  (cla-core/set-connection!)
  (database/create "GraphTestDB" []))

(defn teardown []
  (pprint (database/delete "GraphTestDB")))

(defn fixture [f]
  (setup)
  (f)
  (teardown))

(use-fixtures :once fixture)

(deftest graph-and-query-test

  (testing "get db info list"
    (println "\nlist all available databases")
    (pprint (database/get-info-list)))
  
  (testing "set db context"
    (with-db "GraphTestDB"
      
      (testing "create a vertex and an edge collection and then a graph out of it"
        (println "\ncreate vertex and edge collections 'people' and 'connections'")
        (collection/create :people {"type" 2})
        (collection/create :connections {"type" 3})
        (println "\nnow list all available collections, excluding the system collections")
        (pprint (database/get-collection-info-list {"excludeSystem" true}))
        (println "\ncreate graph 'test-graph'")
        (pprint (graph/create "test-graph" :people :connections))
        (println "\nnow get all available graphs")
        (pprint (database/get-all-graphs))
        (println "\ntest if graph exists\n")
        (is (database/graph-exists? "test-graph")))

      (testing "set graph context and create vertices"
        (with-graph :test-graph
              
          (let [bob (graph/create-vertex {:_key "bob" :name "Bob" :age 28} :people)
                peter (graph/create-vertex {:_key "peter" :name "Peter" :age 25} :people)]

            (testing "create vertices"
              (graph/create-vertex {:_key "clara" :name "Clara" :age 29} :people)
              (graph/create-vertex {:_key "jessica" :name "Jessica" :age 23} :people)
              (graph/create-vertex {:_key "alice" :name "Alice" :age 20} :people))
              
            (testing "perform query: find all people who are older than 24"
              (pprint (query/validate "FOR p IN people FILTER p.age > 24 RETURN p"))
              (pprint (query/explain "FOR p IN people FILTER p.age > 24 RETURN p"))
              (pprint (query/execute "FOR p IN people FILTER p.age > 24 RETURN p")))
              
                    
            (let [edge-key (get (graph/create-edge-with-key {:content "some content"} :somegreatedgekey "friend" "alice" "clara") "_key")]
                      
              (testing "create edges"
                (graph/create-edge {:$label "friend" :_key "edgekey1"} peter :alice)
                (graph/create-edge-with-key {:content "some content"} nil "friend" "clara" "jessica")
                (graph/create-edge-with-key {:content "some content"} nil "boyfriend" :alice bob)
                (graph/create-edge-with-key {:content "some content"} nil "girlfriend" bob :alice))
          
              (testing "update edge"
                (pprint (graph/update-edge {:description "Peter and Alice have been friends for over 6 years"} edge-key)))
                      
              (testing "traversals" 
                (pprint (graph/execute-vertex-traversal "peter" 10 10 true nil))
                (println "\nget all edges that are outgoing from the vertex 'peter'")
                (pprint (graph/execute-edge-traversal "peter" 10 10 true nil))
                (println "\nexecute a graph traversal")
                (pprint (graph/execute-traversal "peter" "people" "connections" "inbound")))
                      
              (testing "delete edge"
                (pprint (graph/delete-edge edge-key))))))
        
        (testing "delete vertex" 
          (println "\ndelete one vertex")
          (pprint (graph/delete-vertex "peter" :test-graph)))
        
        (testing "delete graph" 
          (println "\ndelete the graph")
          (pprint (graph/delete :test-graph)))

        (testing "test nested graph/collection creation"
          (println "test nested graph/collection creation")
          (pprint (graph/create :test-graph-2 (collection/create :people-2 {"type" 2}) (collection/create :connections-2 {"type" 3}))))))))

(deftest misc-test

  (testing "misc functions with output"
    (println "\nversion without details:")
    (pprint (misc/version))
    (println "\nversion with details:")
    (pprint (misc/version true))))
