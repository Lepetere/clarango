(ns clarango.main
	(:require [clj-http.client :as http] ;; https://github.com/dakrone/clj-http
		        [cheshire.core :refer :all]) ;; https://github.com/dakrone/cheshire for custom coding
	(:require [clarango.core :as clarango.core]
				[clarango.collection :as collection]
				[clarango.document :as document]
				[clarango.test :as test])
	(:use clojure.pprint))

(defn -main []
  (clarango.core/connect! {:connection-url  "http://localhost:8529/"})
	(test/run-simple-test))