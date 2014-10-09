(ns clarango.test.graph
  (:require [clojure.test :refer :all]
            [clarango.core :as cla-core]
            [clarango.collection :as collection]
            [clarango.database :as database]
            [clarango.query :as query]
            [clarango.graph :as graph]
            [clarango.admin :as admin])
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

(deftest graph-test
  (println "\nlist all available databases")
  (pprint (database/get-info-list))
  (println "\nperform next operations in the context of 'GraphTestDB'")
  (with-db "GraphTestDB"
    (println "\ncreate vertex and edge collections 'people' and 'connections'")
    (collection/create :people {"type" 2})
    (collection/create :connections {"type" 3})
    (println "\nnow list all available collections, excluding the system collections")
    (pprint (database/get-collection-info-list {"excludeSystem" true}))
    (println "\ncreate graph 'test-graph'")
    (pprint (graph/create "test-graph" :people :connections))
    (println "\nnow get all available graphs")
    (pprint (database/get-all-graphs))
    (println "\nperform next operations in the context of the graph 'test-graph'")
    (with-graph :test-graph
      (println "\ncreate vertices 'Peter', 'Bob', 'Clara', 'Jessica', 'Alice' with :ages")
        (let [bob (graph/create-vertex {:_key "bob" :name "Bob" :age 28})
              peter (graph/create-vertex {:_key "peter" :name "Peter" :age 25})]
            (graph/create-vertex {:_key "clara" :name "Clara" :age 29})
            (graph/create-vertex {:_key "jessica" :name "Jessica" :age 23})
            (graph/create-vertex {:_key "alice" :name "Alice" :age 20})
      
            (println "\n\n---- perform query: find all people who are older than 24\nfirst validate the query, then explain (how the query would be executed on the server), then actually execute it ----\n")
            (pprint (query/validate "FOR p IN people FILTER p.age > 24 RETURN p"))
            (pprint (query/explain "FOR p IN people FILTER p.age > 24 RETURN p"))
            (pprint (query/execute "FOR p IN people FILTER p.age > 24 RETURN p"))
      
            (println "\ncreate edges with labels 'friend', 'boyfriend', 'girlfriend'; save one key to use this edge later")
            (let [edge-key (get (graph/create-edge-with-key {:content "some content"} :somegreatedgekey "friend" "alice" "clara") "_key")]
              (graph/create-edge {:$label "friend" :_key "edgekey1"} peter :alice)
              (graph/create-edge-with-key {:content "some content"} nil "friend" "clara" "jessica")
              (graph/create-edge-with-key {:content "some content"} nil "boyfriend" :alice bob)
              (graph/create-edge-with-key {:content "some content"} nil "girlfriend" bob :alice)
              (println "\nget vertices that have connections going from the vertex 'peter'")
              (pprint (graph/execute-vertex-traversal "peter" 10 10 true nil))
              (println "\nupdate one edge")
              (pprint (graph/update-edge {:description "Peter and Alice have been friends for over 6 years"} edge-key))
              (println "\nget all edges that are outgoing from the vertex 'peter'")
              (pprint (graph/execute-edge-traversal "peter" 10 10 true nil))
              (println "\nexecute a graph traversal")
              (pprint (graph/execute-traversal "peter" "people" "connections" "inbound"))
              (println "\ndelete one edge")
              (pprint (graph/delete-edge edge-key)))))
    (println "\ndelete one vertex")
    (pprint (graph/delete-vertex "peter" :test-graph))
    (println "\ndelete the graph")
    (pprint (graph/delete :test-graph)))

  (println "\nFlush:") ;; all admin functions now
  (pprint (admin/flush))
  (println "\nReload:")
  (pprint (admin/reload))
  (println "\nStatistics:")
  (pprint (admin/statistics))
  (println "\nStatistic descriptions:")
  (pprint (admin/stat-desc))
  (println "\nLog")
  (println (admin/log {"upto" 3}))
  (println "\nRole:") ; This is >= V2
  (pprint (admin/role)))
