(ns clarango.core-utility
	(:require [clj-http.client :as http]
		        [cheshire.core :refer :all]))

(defn read-uri [uri] ;; define timeout?
  (println "connection address: " uri)
  (parse-string (:body (http/get uri))))