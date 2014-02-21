(ns clarango.test.core
  (:use clojure.test clarango.core))

(deftest core-test
  (testing "Check the correct connection settings"
    (is (nil? (get-connection)) "con must be nil initially")
    (is (false? (connection-set?)) "con must be nil initially")
    (set-connection!) ;; call without arguments
    (is (= {:db-name "_system", 
            :connection-url "http://localhost:8529/"}
           (get-connection)) "Mandatory default values!")
    (set-connection-url! "http://localhost:9999/")
    (set-default-db! "another-db")
    (set-default-collection! "another-collection")
    (is (= {:db-name "another-db", 
            :connection-url "http://localhost:9999/",
            :collection-name "another-collection"}
           (get-connection)) "obvious receive what has been set")))
