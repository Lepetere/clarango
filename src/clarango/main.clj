(ns clarango.main
	(:require [clarango.core :as clarango.core]
      [clarango.utilities.core-utility :as core-utility]
				[clarango.collection :as collection]
				[clarango.document :as document]
        [clarango.database :as database]
        [clarango.query :as query])
	(:use clojure.pprint)
  (:use clarango.collection-ops))

;; DEMO to call lein run and test it without the lib usage
(defn -main []

  ;;; http batch request debugging
  (clarango.core/set-connection! {:connection-url "http://localhost:8529/", :db-name "_system"})
  (pprint (query/explain "FOR u IN `query-test` LIMIT 2 RETURN u" "_system"))
  (pprint (query/validate "FOR u IN `query-test` LIMIT 2 RETURN u"))
  (pprint (query/execute "FOR u IN `query-test` FILTER u.name == @name RETURN u" {"name" "Peter"}))
  (pprint (query/execute-count "FOR u IN `query-test` RETURN u" 10 true))

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
