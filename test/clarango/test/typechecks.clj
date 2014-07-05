(ns clarango.test.typechecks
  (:use clojure.test clarango.core clarango.utilities.core-utility)
  (:require [clarango.database :as db]
            [clarango.collection :as coll]
            [clarango.document :as doc]))

(deftest typecheck-test
  (testing "Testing if typechecks work"

    (testing "on a document function: "

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