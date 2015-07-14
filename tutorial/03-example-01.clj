(ns myproject.core
  (:require [clarango.core :as cla-core]
            [clarango.database :as db]
            [clarango.collection :as coll]
            [clarango.document :as doc])) 

(defn -main [& args]
  (println ">>> Starting the CRUD Script...")
  (cla-core/set-connection-url! "http://localhost:8529")
  (db/create "testdb" [])
  (cla-core/set-default-db! "testdb")
  (coll/create "testcoll")
  (cla-core/set-default-collection! "testcoll")

  (doc/create {:_key "mykey" :name "Peter" :age "42"})
  (println (doc/get-by-key "mykey"))
  (doc/update-by-key {:name "Peter" :age "21"} "mykey")
  (println (doc/get-by-key "mykey"))

  (println "Look it up under http://localhost:8529/_db/testdb/_admin/aardvark/index.html#collections")
  (Thread/sleep 30000)

  (doc/delete-by-key "mykey")
  (coll/delete "testcoll")
  (db/delete "testdb")
  (println ">>> Finished CRUD!")
  (println "CONNECTION=" (cla-core/get-connection))
)
