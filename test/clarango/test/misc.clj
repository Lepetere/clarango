(ns clarango.test.misc
  (:require [clojure.test :refer :all]
            [clarango.core :as cla-core]
            [clarango.database :as database]
            [clarango.misc :as misc])
  (:use clojure.pprint)
  (:use clarango.core))

(defn setup []
  (cla-core/set-connection!)
  (database/create "MiscTestDB" []))

(defn teardown []
  (database/delete "MiscTestDB"))

(defn fixture [f]
  (setup)
  (f)
  (teardown))

(use-fixtures :each fixture)

(deftest misc-test
  (testing "misc functions with output"
    (println "\nversion without details:")
    (pprint (misc/version))
    (println "\nversion with details:")
    (pprint (misc/version true))))
