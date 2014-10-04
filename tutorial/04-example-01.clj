(ns leanpub.core
  (:require [clarango.core :as cla-core]
            [clarango.database :as db]
            [clarango.collection :as coll]
            [clarango.document :as doc]
            [clarango.query :as query]))

(defn -main [& args]
  (println "Starting a simple query demo!")
  
  (cla-core/set-connection-url! "http://localhost:8529")
;  (db/create "querydb"  [])
  (cla-core/set-default-db! "querydb")
;  (coll/create "qcoll")
  (cla-core/set-default-collection! "qcoll")

;  (doc/create {:_key "key1" :name "Tom" :age 50})
;  (doc/create {:_key "key2" :name "Dave" :age 55})
;  (doc/create {:_key "key3" :name "Wil" :age 35})
;  (doc/create {:_key "key4" :name "Agnes" :age 28})
   
  (println "\n" (query/validate "FOR q1 IN qcoll RETURN q1"))
  (println "\n" (query/explain "FOR q1 IN qcoll RETURN q1"))
  (println "\n" (query/execute "FOR q1 IN qcoll RETURN q1"))

  (println "\n" (query/execute "FOR q2 IN qcoll FILTER q2.age > 45 RETURN q2.name"))

  (println "Finished!")
  )
; {bindVars [], collections [qcoll], error false, code 200}

; {result [{_id qcoll/key1, _rev 417601419, _key key1, age 50, name Tom}
           {_id qcoll/key2, _rev 417863563, _key key2, age 55, name Dave}
           {_id qcoll/key3, _rev 418060171, _key key3, age 35, name Wil}
           {_id qcoll/key4, _rev 418256779, _key key4, age 28, name Agnes}],
           hasMore false, error false, code 201}

; {result [Tom Dave], hasMore false, error false, code 201}

