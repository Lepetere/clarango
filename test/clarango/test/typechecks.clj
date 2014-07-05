(ns clarango.test.typechecks
  (:use clojure.test clarango.core clarango.utilities.type-utility clarango.utilities.core-utility)
  (:require [clarango.database :as db]
            [clarango.collection :as coll]
            [clarango.document :as doc]))

(deftest typecheck-test
  (testing "Testing if typechecks work: "

    #_(testing "typecheck functions on their own"
      (is (= "test-string" (ensure-string "test-string")) "Does not let string pass")
      (is (thrown? Exception (ensure-string {:property "test-string"})) "Must throw exception for map")
      (is (thrown? Exception (ensure-string nil)) "Must throw exception for nil"))

    #_(testing "clarango.utilities.core-utilities methods for return types"
      (is (= clojure.lang.PersistentArrayMap (type (filter-out-map [0, 1, 2, {:name "map"}]))) "filter-out-map should only return maps")
      (is (let [result-vec (ensure-string-vector (remove-map ["0", "1", "2", {:name "map"}]))]
            (and 
              (and (= (type (get result-vec 0)) java.lang.String) (= (type (get result-vec 1)) java.lang.String))
               (= (type (get result-vec 2)) java.lang.String)))
        "remove-map in combination with ensure-string-vector should return a vector that only contains strings")
      (is (thrown? Exception (ensure-string-vector (remove-map ["0", nil, 2, {:name "map"}]))) "ensure-string-vector should throw an error if called on an array, that contains other types than strings"))

    (testing "full example on a document function: "

      ;; setup
      (set-connection!)
      (db/create "typecheck-test-db" [])
      (set-default-db! "typecheck-test-db")
      (coll/create "typecheck-test-collection")
      (set-default-collection! "typecheck-test-collection")

      (testing "failing tests"
        (is (thrown? java.lang.AssertionError (doc/create-with-key "test document" {:name "test document"})))
        (is (thrown? java.lang.AssertionError (doc/create-with-key "test document" "test document")))
        (is (thrown? java.lang.AssertionError (doc/create-with-key {:name "test document"} {:name "test document"})))
        (is (thrown? java.lang.AssertionError (doc/create-with-key "test document" nil)))
        (is (thrown? java.lang.AssertionError (doc/create-with-key nil {:name "test document"}))))

      (testing "working tests"
        (is (= false (get (doc/create-with-key {:name "test document"} "test-document") "error"))))

      (db/delete "typecheck-test-db"))))