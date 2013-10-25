# Some ideas #

* **CouchDB** clutch https://github.com/clojure-clutch/clutch
* **ElasticSearch** elastisch https://github.com/clojurewerkz/elastisch/blob/master/src/clojurewerkz/elastisch/rest.clj
* **Neo4j** neocons https://github.com/michaelklishin/neocons

## API Design ##
* db handle creation
* create = POST
* read = GET / collections
* delete = DELETE (find-delete)
* update = PUT (find-update) (partial-update = PATCH)
* Edges
* AQL queries (string-based, parameter based, -> nested / pipe based)
* Others as: Index, Cap, Hash, Skip-List, Geo, Fulltext, Transaction, Graphs,
bulk, Batch, User-Functions, Admin / monitor, User-Management, misc, 

##Libraries to help:
* REST: Liberator, clj-http, clj-http.client, DAKRONE, http.async.client
* JSON: cheshire.core, data.json
* URLS: java.net.URLEncoder

## Flow
* Build REST  String
* Convert to URL
* Fire URL
* get result
* convert to Clojure Types
* Error handling (http 412)

We should code against 1.4 because of multiple databases

Testing with CURL http://curl.haxx.se/download.html
curl -X get http://localhost:8529/_api/document/persons/21855193 // simply works :-)

Structure:
OPERATION | REST-PARAMS | LOCATION | APIDOCSTRING | [DATABASE] | COLLECTION | KEY (OPTIONAL REVISION ?)