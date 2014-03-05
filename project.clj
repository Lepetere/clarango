(defproject clarango "0.3.0"
  :description "A Clojure client for the HTTP API of ArangoDB"
  :url "https://github.com/edlich/clarango"
  :license {:name "Apache License 2.0"
            :url "http://www.apache.org/licenses/"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-http "0.7.7"]
                 [cheshire "5.3.1"]]
  :codox {:exclude [clarango.utilities.core-utility
                    clarango.utilities.http-utility
                    clarango.utilities.uri-utility]
          :src-dir-uri "https://github.com/edlich/clarango/blob/master/"
          :src-linenum-anchor-prefix "L"}
  :plugins [[codox "0.6.7"]]
  :main clarango.main)
