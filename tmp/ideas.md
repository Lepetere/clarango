# Some ideas #

* **CouchDB** clutch https://github.com/clojure-clutch/clutch
* **ElasticSearch** elastisch https://github.com/clojurewerkz/elastisch/blob/master/src/clojurewerkz/elastisch/rest.clj
* **Neo4j** neocons https://github.com/michaelklishin/neocons

## API Design ##
* db handle creation
* read
* delete (find-delete)
* update (find-update)
* query (string-based, parameter based, -> nested / pipe based)

##Libraries to help:
* REST: Liberator, clj-http, clj-http.client, DAKRONE, http.async.client
* JSON: cheshire.core, data.json
* URLS: java.net.URLEncoder

## Flow
* Build Datastructure
* Convert to URL
* Fire URL
* get result
* convert to Clojure Types