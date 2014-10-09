(defproject clarango "0.5.0"
  :description "A Clojure client for the HTTP API of ArangoDB"
  :url "https://github.com/edlich/clarango"
  :license {:name "Apache License 2.0"
            :url "http://www.apache.org/licenses/"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-http "1.0.0"]
                 [cheshire "5.3.1"]]
  :plugins [[codox "0.8.0"]]
  :codox  {:src-dir-uri "https://github.com/edlich/clarango/blob/master/"
           :src-linenum-anchor-prefix "L"
           :exclude [clarango.utilities.core-utility
                     clarango.utilities.http-utility
                     clarango.utilities.uri-utility]})