(defproject clarango "0.4.1"
  :description "A Clojure client for the HTTP API of ArangoDB"
  :url "https://github.com/edlich/clarango"
  :license {:name "Apache License 2.0"
            :url "http://www.apache.org/licenses/"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-http "0.7.7"]
                 [cheshire "5.3.1"]]
;;  :plugins [[lein-marginalia "0.7.1"]]
  :plugins [[codox "0.8.0"]]
  :codox  {:src-dir-uri "https://github.com/edlich/clarango/blob/master/"
           :src-linenum-anchor-prefix "L"
           :exclude [clarango.core-utility
                     clarango.http-utility
                     clarango.uri-utility]}
 )
