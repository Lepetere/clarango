(ns clarango.test.transaction
    (:require [clojure.test :refer :all]
              [clarango.core :as cla-core]
              [clarango.database :as database]
              [clarango.collection :as cla-coll]
              [clarango.transaction :as cla-trans])
    (:use clojure.pprint))

;; All tests are running against the default _system database
(defn setup []
    (cla-core/set-connection!)
    (cla-coll/create "products")
    (cla-coll/create "users"))

(defn teardown []
    (cla-coll/delete "products")
    (cla-coll/delete "users"))

(defn fixture [f]
    (setup)
    (f)
    (teardown))

(use-fixtures :each fixture)

(defn to-map
  [data]
    (into {} (map (fn [[k v]] [(keyword k) v]) data)))

(deftest collection-definition
    ; Test data lifted from the ArangoDB docs
    (def trans-code "function () {
                        var db = require('internal').db;
                        db.products.save({});
                        return db.products.count(); }")

    (testing "Collection name can be defined as a single string"
        (let [result (to-map (cla-trans/execute
            "products" trans-code))]
        (is (= (:code result) 200))))

    (testing "Collection name can be defined as a vector of strings"
        (let [result (to-map (cla-trans/execute
            ["products"] trans-code))]
        (is (= (:code result) 200))))

    (testing "Collection can be defined as map of single string with explicit scoping [:read/:write]"
        (let [result (to-map (cla-trans/execute
            {:write "products"} trans-code))]
        (is (= (:code result) 200))))

    (testing "Collection can be defined as map of string vector with explicit scoping"
        (let [result (to-map (cla-trans/execute
            {:write ["products"]} trans-code))]
        (is (= (:code result) 200)))))

(deftest return-values

    (testing "Return value from transaction function is returned as 'result' in response map"
        (let [result (to-map (cla-trans/execute
            "products" "function () { return 'foobar' }"))]
            (is (= (:result result) "foobar"))))

    (testing "Multiple input statements returns expected count as 'result' in reponse map"
        (let [no-of-entries (rand-int 100)  ; Random number of entries, up to 100
              trans-function (str "function () {
                                  var db = require('internal').db; "
                                  (apply str (take no-of-entries (repeat "db.products.save({}); ")))
                                  "return db.products.count(); }")
              result (to-map (cla-trans/execute
                  "products" trans-function))]
        (is (= (:result result) no-of-entries)))))
