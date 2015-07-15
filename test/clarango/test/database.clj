(ns clarango.test.database
  (:use clojure.test clarango.core)
  (:require [clarango.database :as db]))

(deftest database-test
  (testing "Testing if the database management works.")
  (set-connection!) ;; use default address 
  (is (> (count (db/get-info-list)) 0) "At least one db should exist!") 

  (def testdb "clatest")
  (db/create testdb []) ;; Take care: any errors might leave the created db
  (is (db/database-exists? testdb) "DB must now exist!")
  (println "*** Database List: " (db/get-info-list))

  (set-default-db! testdb)
  (println "*** Info Current <" (db/get-info-current) ">") ; yields info on _system or current?

  ;; Windows shows: "error 400 "bad parameter"!
  (println "*** User Access DBs: <" (db/get-info-user) ">") 

  (println "Collections in the default DB: <" (db/get-collection-info-list) ">")
  (println "Collections in the testDB: <" (db/get-collection-info-list testdb) ">")

  (db/delete testdb)
  (is (> (count (db/get-info-list)) 0) "The systen must be in the original state!"))
