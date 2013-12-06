(defproject clarango "0.1.0-SNAPSHOT"
  :description "A Clojure client for the HTTP API of ArangoDB"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-http "0.7.7"]
                 [cheshire "5.2.0"]]
  :main clarango.main) ;; for testing without a lib
