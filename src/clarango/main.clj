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

  ;;; http batch request debugging
  (clarango.core/set-connection! {:connection-url "http://localhost:8529/", :db-name "_system"})
  (println "\n\ntest query methods:\n")
  (pprint (query/explain "FOR u IN `query-test` LIMIT 2 RETURN u" "_system"))
  (pprint (query/validate "FOR u IN `query-test` LIMIT 2 RETURN u"))

  (println "\n\ntest graph methods:\n")
  (do (collection/create "vertices1" "GraphTestDB" {"type" 2})
  (collection/create "edges1" "GraphTestDB" {"type" 3})
  (pprint (graph/create "test-graph-1" "vertices1" "edges1" "GraphTestDB" {"waitForSync" true}))
  (with-db "GraphTestDB"
    (with-graph "test-graph-1"
      (pprint (graph/create-vertex {:_key "vertex1", :data "blabla"}))
      (pprint (graph/get-vertex "vertex1"))
      (graph/create-vertex {:_key "vertex2", :data "blubber"})
      (pprint (graph/create-edge {:data "blubber-connection-data"} "blubber-connection" "vertex1" "vertex2"))))
  (pprint (graph/get-info "test-graph-1" "GraphTestDB"))
  (pprint (database/get-all-graphs "GraphTestDB")))
  (pprint (graph/delete "test-graph-1" "GraphTestDB"))

  #_(pprint (document/create-multi [{:name "test1"} {:name "test2"} {:name "test3"} {:name "test4"} {:name "test5"}] "test-collection" "_system"))
  #_(
  ;;; test collection methods
  (println "\n\ntest collection methods:\n")
  (clarango.core/set-connection!)
  (pprint (collection/create "test-collection" "_system"))
  (pprint (document/create {:name "test"} "test-collection" "_system"))
  #_(pprint (document/create-multi [{:name "test1"} {:name "test2"} {:name "test3"} {:name "test4"} {:name "test5"}] "test-collection" "_system"))
  (pprint (collection/load {"count" false} "test-collection"))
  (pprint (collection/get-all-documents "test-collection"))
  (pprint (collection/get-extended-info-figures "test-collection"))
  (pprint (collection/unload "test-collection"))
  (pprint (collection/rotate "test-collection"))
  (pprint (collection/truncate "test-collection"))
  (pprint (collection/rename "test-collection-dos" "test-collection"))
  (pprint (collection/get-extended-info-count "test-collection-dos"))

  ;;; test clojure idiomatic collection methods
  (println "\n\ntest clojure idiomatic collection methods:\n")
  (pprint (cla-assoc! "test-collection-dos" "bla" {:name "Bla bla dokument"}))
  (pprint (cla-conj! "test-collection-dos" {:quatsch "Mit Soße"}))
  (pprint (clarango.collection/get-all-documents "test-collection-dos"))
  (pprint (cla-dissoc! "test-collection-dos" "bla"))
  #_(pprint (cla-get! "test-collection-dos" "bla"))

  (pprint (collection/delete "test-collection-dos" "_system"))

  ;;; test database methods
  (println "\n\ntest database methods:\n")
  (clarango.core/set-connection!)
  (pprint (database/create "new-test-database" []))
  (pprint (database/get-info-current))
  (pprint (database/get-info-list))
  (pprint (database/get-info-user))
  (pprint (database/get-collection-info-list))
  (pprint (database/delete "new-test-database"))))