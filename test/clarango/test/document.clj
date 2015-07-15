(ns clarango.test.document
  (:require [clojure.test :refer :all]
            [clarango.core :as cla-core]
            [clarango.collection :as collection]
            [clarango.document :as document]
            [clarango.database :as database])
  (:use clojure.pprint)
  (:use clarango.collection-ops)
  (:use clarango.core))

(defn setup []
  (cla-core/set-connection!)
  (pprint (meta (database/create "test-DB" [{:username "test-user"}])))
  (cla-core/set-default-db! "test-DB")
  )

(defn teardown []
  (pprint (database/delete "test-DB")))

(defn fixture [f]
  (setup)
  (f)
  (teardown))

(use-fixtures :once fixture)

(deftest document-test

  (testing "create collection"
    (is (false? (database/collection-exists? "test-collection" "test-DB")))
    (pprint (meta (collection/create "test-collection" "test-DB")))
    (println "\ntest if collection exists\n")
    (is (database/collection-exists? "test-collection" "test-DB")))

  (testing "document CRUD"
    (pprint (document/create-with-key {:name "some test document"} :test-doc :test-collection :test-DB))
    (pprint (document/update-by-key {:additional "some additional info"} :test-doc :test-collection :test-DB))
    (pprint (document/get-by-key :test-doc :test-collection :test-DB))
    (pprint (document/replace-by-example {:name "new version of our test document"} {:additional "some additional info"} :test-collection :test-DB))))

(deftest nested-document-creation

  (testing "create a document with a nested collection/document create call"
    (pprint (document/create-with-key {:description "what a great way to create a document"} :new-doc
      (collection/create "a-nested-collection"))))

  (testing "create a document with a nested db/collection create call"
    (with-db (database/create "nested-test-DB" [])
      (pprint (document/create-with-key {:description "what a great way to create a document"} :new-doc
      (collection/create "a-nested-collection"))))
      (database/delete "nested-test-DB")))

(deftest collection-ops-test

  (testing "set default DB"
    (cla-core/set-default-db! "test-DB"))
  (collection/create "test-collection-0" "test-DB")
  (testing "collection ops: assoc, dissoc, conj"
    (pprint (cla-assoc! "test-collection-0" "new-document-1" {:description "some test document to test the clojure idiomatic collection methods" :key-type "given key"}))
    (pprint (cla-conj! "test-collection-0" {:description "some test document to test the clojure idiomatic collection methods" :key-type "auto generated key"}))
    (pprint (cla-get! "test-collection-0" "new-document-1"))
    (pprint (cla-dissoc! "test-collection-0" "new-document-1"))))

(deftest collection-test
  (collection/create "test-collection-1" "test-DB")
  (testing "get collection information"
    (pprint (collection/get-info "test-collection-1"))
    (pprint (collection/get-all-documents "test-collection-1")))

  (testing "get delayed collection and delayed documents with explicit collection and db"
    (println "\ndelayed collection test 1")
    (let [delayed-collection (collection/get-delayed-collection "test-collection-1" "test-DB")
          _ (pprint delayed-collection)]
      (doseq [k (keys delayed-collection)]
        (println "\nget document" k ":")
        (pprint @(get delayed-collection k)))))

  (testing "get delayed collection and delayed documents with default collection and db"
    (println "\ndelayed collection test 2")
    (with-db "test-DB"
      (with-collection "test-collection-1"
        (let [delayed-collection (collection/get-delayed-collection)
                    _ (pprint delayed-collection)]
                (doseq [k (keys delayed-collection)]
                  (println "\nget document" k ":")
                  (pprint @(get delayed-collection k)))))))

  (testing "rename the collection and modify it's properties"
    (pprint (collection/rename "new-name-test-collection" "test-collection-1"))
    (pprint (collection/modify-properties {"waitForSync" true} "new-name-test-collection"))
    (pprint (collection/get-extended-info-figures "new-name-test-collection")))

  (testing "unload and delete collection"
    (pprint (collection/unload-mem "new-name-test-collection"))
    (pprint (collection/delete "new-name-test-collection")))
  )
