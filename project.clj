(defproject clarango "0.7.0"
  :description "A Clojure client for the HTTP API of ArangoDB"
  :url "https://github.com/edlich/clarango"
  :license {:name "Apache License 2.0"
            :url "http://www.apache.org/licenses/"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clj-http "1.1.2"]
                 [cheshire "5.5.0"]]
  :plugins [[codox "0.8.0"]]
  :codox  {:src-dir-uri "https://github.com/edlich/clarango/blob/master/"
           :src-linenum-anchor-prefix "L"
           :exclude [clarango.utilities.core-utility
                     clarango.utilities.http-utility
                     clarango.utilities.uri-utility]})
