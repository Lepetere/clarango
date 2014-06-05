(ns clarango.test.index
  (:require [clojure.test :refer :all]
            [clarango.core :as clacore]
            [clarango.database :as db]
            [clarango.collection :as collection]
            [clarango.index :as index]))

(defn setup []
  (clacore/set-connection!)
  (db/create "clatest" [])
  (clacore/set-default-db! "clatest"))

(defn teardown []
  (db/delete "clatest"))

(defn fixture [f]
  (setup)
  (f)
  (teardown))

(use-fixtures :once fixture)

(deftest index-test
  (testing "get-all-indexes"
    (collection/create "col")
    (let [result (collection/get-all-indexes "col")]
      (is (= (count result) 1))
      (is (= (get (first result) "type") "primary"))))

  (testing "get-by-key"
    (let [i1 (first (collection/get-all-indexes "col"))
          i2 (index/get-by-key (i1 "id"))]
      (is (= (i1 "type" (i2 "type"))))))

  (testing "create index"
    (index/create {"type" "cap"
                   "size" 10} "col")
    (is (= (count (collection/get-all-indexes "col")) 2)))

  (testing "delete index"
    (is (= (count (collection/get-all-indexes "col")) 2))
    (let [idx ((second (collection/get-all-indexes "col")) "id")]
      (println "delete index")
      (println idx)
      (index/delete-by-key idx)
    (is (= (count (collection/get-all-indexes "col")) 1)))))


