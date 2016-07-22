(defproject clarango "0.7.1"
  :description "A Clojure client for the HTTP API of ArangoDB"
  :url "https://github.com/lepetere/clarango"
  :license {:name "Apache License 2.0"
            :url "http://www.apache.org/licenses/"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clj-http "2.1.0"]
                 [cheshire "5.5.0"]]
  :plugins [[lein-codox "0.9.0"]]
  :codox  {:src-dir-uri "https://github.com/lepetere/clarango/blob/master/"
           :src-linenum-anchor-prefix "L"
           :exclude [clarango.utilities.core-utility
                     clarango.utilities.http-utility
                     clarango.utilities.uri-utility]})
