(ns clarango.test.function
    (:require [clojure.test :refer :all]
              [clarango.core :as cla-core]
              [clarango.database :as database]
              [clarango.function :as cla-func])
    (:use clojure.pprint))

; Test data (lifted from the ArangoDB docs)
(def func-name "myfunctions::temperature::celsiustofahrenheit")
(def func-code "function (celsius) { return celsius * 1.8 + 32; }")

;; Functions are created under _system so we don't need to define
;; our own database when creating them
(defn setup []
    (cla-core/set-connection!))

(defn fixture [f]
    (setup)
    (f))

(use-fixtures :each fixture)

(defn to-map
  [data]
    (into {} (map (fn [[k v]] [(keyword k) v]) data)))

(deftest create-functions-test

    (testing "Creating non existing function (by name) returns 201"
        (let [result (to-map (cla-func/new-function
            func-name func-code))]
            (is (:code result) 201)))

    (testing "Updating existing function (by name) returns 200"
        (let [result (to-map (cla-func/new-function
            func-name func-code))]
            (is (:code result) 200)))

    ;; ArangoDB doesn't appear to have a function to clear down,
    ;;so we need to do it ourselves.
    (cla-func/delete-function func-name))

(deftest get-functions-list

    (cla-func/new-function func-name func-code)

    (testing "GET all functions returns a list"
        (let [result (cla-func/get-functions)]
            (is (true? (seq? result)))))

    (testing "GET all functions returns function name as 'name'"
        (let [result (to-map (first (cla-func/get-functions)))]
            (is (= func-name (:name result)))))

    (testing "GET all functions returns function code as 'code'"
        (let [result (to-map (first (cla-func/get-functions)))]
            (is (= func-code (:code result)))))

    (cla-func/delete-function func-name))
