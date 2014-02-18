(ns clarango.main
	(:require [clarango.core :as clarango.core]
      [clarango.utilities.core-utility :as core-utility]
				[clarango.collection :as collection]
				[clarango.document :as document]
        [clarango.database :as database])
	(:use clojure.pprint)
  (:use clarango.collection-ops))

;; DEMO to call lein run and test it without the lib usage
(defn -main []

  ;;; test collection methods
  (println "\n\ntest collection methods:\n")
  (clarango.core/set-connection!)
  (pprint (clarango.collection/create "test-collection" "_system"))
  (pprint (clarango.document/create {:name "test"} "test-collection" "_system"))
  (pprint (clarango.collection/load {"count" false} "test-collection"))
  (pprint (clarango.collection/get-all-documents "test-collection"))
  (pprint (clarango.collection/get-extended-info-figures "test-collection"))
  (pprint (clarango.collection/unload "test-collection"))
  (pprint (clarango.collection/rotate "test-collection"))
  (pprint (clarango.collection/truncate "test-collection"))
  (pprint (clarango.collection/rename "test-collection-dos" "test-collection"))
  (pprint (clarango.collection/get-extended-info-count "test-collection-dos"))

  ;;; test clojure idiomatic collection methods
  (println "\n\ntest clojure idiomatic collection methods:\n")
  (pprint (assoc! "test-collection-dos" "bla" {:name "Bla bla dokument"}))
  (pprint (conj! "test-collection-dos" {:quatsch "Mit Soße"}))
  (pprint (clarango.collection/get-all-documents "test-collection-dos"))
  (pprint (dissoc! "test-collection-dos" "bla"))
  #_(pprint (get! "test-collection-dos" "bla"))

  (pprint (clarango.collection/delete "test-collection-dos" "_system"))

  ;;; test database methods
  (println "\n\ntest database methods:\n")
  (clarango.core/set-connection!)
  (pprint (clarango.database/create "new-test-database" []))
  (pprint (clarango.database/get-info-current))
  (pprint (clarango.database/get-info-list))
  (pprint (clarango.database/get-info-user))
  (pprint (clarango.database/get-collection-info-list))
  (pprint (clarango.database/delete "new-test-database")))