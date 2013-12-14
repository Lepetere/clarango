(ns clarango.test.core
  (:use clojure.test))

(deftest core-test
  (testing "Check if it runs on travis-ci"
    (is (= 1 1))))
