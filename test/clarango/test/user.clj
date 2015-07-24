(ns clarango.test.user
  (:require [clojure.test :refer :all]
            [clarango.core :as cla-core]
            [clarango.database :as database]
            [clarango.user :as cla-user])
  (:use clojure.pprint))

 (defn setup []
   (cla-core/set-connection!)
   (pprint (meta (database/create "test-DB" [{:username "test-user"}])))
   (cla-core/set-default-db! "test-DB"))

(defn teardown []
  (pprint (database/delete "test-DB")))

(defn fixture [f]
  (setup)
  (f)
  (teardown))

(use-fixtures :once fixture)

(defn to-map
  [data]
    (into {} (map (fn [[k v]] [(keyword k) v]) data)))

(def test-user {:username "username" :passwd "secret"})

(deftest user-test
  (testing "User does not currently exist"
    (is (false? (cla-user/exists? (:username test-user)))))

  (testing "Create a new user with minimum required fields (name) ..."
    (let [result (to-map (cla-user/create {:user (:username test-user)}))]
      (is (= (:username test-user) (:user result)))))

  (testing "User exists ..."
    (is (true? (cla-user/exists? (:username test-user)))))

  (testing "User is set to active == true by default ..."
    (let [result (to-map (cla-user/get-by-username (:username test-user)))]
      (is (= true (:active result)))))

  (testing "User can be found by username once created ..."
    (let [result (to-map (cla-user/get-by-username (:username test-user)))]
      (is (= (:username test-user) (:user result)))))

  (testing "Extras user data can be set after initial creation ..."
    (let [result (to-map (cla-user/update-by-username {:extra {:bio "Loves Spongebob Squarepants"}} (:username test-user)))]
      (is (= "Loves Spongebob Squarepants" ((:extra result) "bio")))))

  (testing "Extras user data can be replaced after being set ..."
    (let [result (to-map (cla-user/update-by-username {:extra {:bio "Loves Spongebob Squarepants. Prefers Adventure Time"}} (:username test-user)))]
      (is (= "Loves Spongebob Squarepants. Prefers Adventure Time" ((:extra result) "bio")))))

  (testing "User can be deleted once created (returns :code 202)"
    (let [delete-result (to-map (cla-user/delete-by-username (:username test-user)))]
      (is (= 202 (:code delete-result)))))

  (testing "User no longer exists"
    (is (false? (cla-user/exists? (:username test-user))))))
