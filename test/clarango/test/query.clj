(ns clarango.test.query
  (:require [clojure.test :refer :all]
            [clarango.core :as cla-core]
            [clarango.document :as document]
            [clarango.collection :as collection]
            [clarango.database :as database]
            [clarango.query :as query])
  (:use clojure.pprint)
  (:use clarango.core))

; Test data (lifted from the ArangoDB docs)
(def func-name "myfunctions::temperature::celsiustofahrenheit")
(def func-code "function (celsius) { return celsius * 1.8 + 32; }")

(defn setup []
  (cla-core/set-connection!)
  (database/create "QueryTestDB" []))

(defn teardown []
  (database/delete "QueryTestDB"))

(defn fixture [f]
  (setup)
  (f)
  (teardown))

(use-fixtures :each fixture)

(defn to-map
  [data]
    (into {} (map (fn [[k v]] [(keyword k) v]) data)))

(deftest graph-and-query-and-traversal-test

  (testing "perform query: find all people who are older than 24"
    (with-db "QueryTestDB"
      ;; create test data
      (collection/create :people {"type" 2})
      (document/create-with-key {:name "Bob" :age 28} "bob-key" :people)
      (document/create-with-key {:name "Peter" :age 25} "peter-key" :people)
      ;; perform query tests
      (is (= false (get (query/validate "FOR p IN people FILTER p.age > 24 RETURN p") "error")))
      (let [explain-result (query/explain "FOR p IN people FILTER p.age > 24 RETURN p")]
        (is (every? true? [(contains? explain-result "nodes") (contains? explain-result "rules") (contains? explain-result "collections") (contains? explain-result "variables")])))
      (let [execute-result (query/execute "FOR p IN people FILTER p.age > 24 RETURN p")]
        (is (= false (get execute-result "hasMore")))
        (is (= 2 (count (get execute-result "result"))))))))

(deftest create-functions-test

  (testing "Creating non existing function (by name) returns 201"
    (let [result (to-map (query/new-function
          func-name func-code))]
      (is (:code result) 201)))

  (testing "Updating existing function (by name) returns 200"
    (let [result (to-map (query/new-function
          func-name func-code))]
      (is (:code result) 200)))

  ;; ArangoDB doesn't appear to have a function to clear down,
  ;;so we need to do it ourselves.
  (query/delete-function func-name))

(deftest get-functions-list
  
  (query/new-function func-name func-code)

  (testing "GET all functions returns a list"
    (let [result (query/get-functions)]
      (is (true? (seq? result)))))

  (testing "GET all functions returns function name as 'name'"
    (let [result (to-map (first (query/get-functions)))]
      (is (= func-name (:name result)))))

  (testing "GET all functions returns function code as 'code'"
    (let [result (to-map (first (query/get-functions)))]
      (is (= func-code (:code result)))))

    (query/delete-function func-name))
