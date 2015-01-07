(ns clarango.misc
  (:require [clarango.utilities.http-utility :as http])
  (:use [clarango.utilities.uri-utility :only [build-resource-uri]]))

(defn version
  "Returns the version of the ArangoDB server.
  
  Takes as only argument an optional 'details' flag which indicates if you want to receive 
  additional information about included components and their versions"
  ([]
    (http/get-uri [:body] (build-resource-uri "version?details=false" nil nil "_system")))
  ([details]
    (http/get-uri [:body] (build-resource-uri (str "version?details=" details) nil nil "_system"))))