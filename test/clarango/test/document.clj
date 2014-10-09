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
  (pprint (database/create "test-DB" [{:username "test-user"}]))
  (cla-core/set-default-db! "test-DB"))

(defn teardown []
  (pprint (database/delete "test-DB")))

(defn fixture [f]
  (setup)
  (f)
  (teardown))

(use-fixtures :once fixture)

(deftest document-test

  (println "\ncreate Collection 'test-collection' in DB 'test-DB'")
  (pprint (collection/create "test-collection" "test-DB"))
  (println "\ndocument CRUD")
  (pprint (document/create-with-key {:name "some test document"} :test-doc :test-collection :test-DB))
  (pprint (document/update-by-key {:additional "some additional info"} :test-doc :test-collection :test-DB))
  (pprint (document/get-by-key :test-doc :test-collection :test-DB))
  (pprint (document/replace-by-example {:name "new version of our test document"} {:additional "some additional info"} :test-collection :test-DB))
  

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
  (pprint (collection/unload-mem "new-name-test-collection"))
  (pprint (collection/delete "new-name-test-collection")))
