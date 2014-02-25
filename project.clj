(defproject clarango "0.2.2"
  :description "A Clojure client for the HTTP API of ArangoDB"
  :url "https://github.com/edlich/clarango"
  :license {:name "Apache License 2.0"
            :url "http://www.apache.org/licenses/"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-http "0.7.7"]
                 [cheshire "5.3.1"]]
  :main clarango.main)
