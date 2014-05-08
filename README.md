<!-- img src="https://travis-ci.org/edlich/clarango.png?branch=master" alt="travis-ci.org Build Status" title="Build Status" align="right" /-->
Clarango: a Clojure driver for ArangoDB
========

Clarango is work in progress. The current lib version on clojars is 0.3.2. We expect V 1.0 in late 2014 with a full stable API and all the missing functionality. Also have a look at:
* The [API overview](http://edlich.github.io/clarango/docs/uberdoc.html) of V 0.3.2
* Some examples can be found [here](https://github.com/edlich/clarango/blob/development/src/clarango/main.clj)
* A book as [pdf](https://leanpub.com/clarango) for printing / download or as [html readable online](https://leanpub.com/clarango/read)

The library has not yet been tested against Arango >= 2.0.0

## Features

* various options for connecting
* document CRUD including various options
  -> for documentation on this see [document.clj](https://github.com/edlich/clarango/blob/master/src/clarango/document.clj)
* querying by example
* AQL queries (see [query namespace](https://github.com/edlich/clarango/blob/master/src/clarango/query.clj))
* collection management (see [collection namespace](https://github.com/edlich/clarango/blob/master/src/clarango/collection.clj))
* database management (see [database namespace](https://github.com/edlich/clarango/blob/master/src/clarango/database.clj))
* graph functions (see [graph namespace](https://github.com/edlich/clarango/blob/master/src/clarango/graph.clj))
* simple exception handling
* experimental clojure idiomatic collection methods like `cla-assoc!` and `cla-conj!` (see [collection_ops.clj](https://github.com/edlich/clarango/blob/master/src/clarango/collection_ops.clj) for details)

## Installation

The driver is hosted on [Clojars](https://clojars.org/clarango). Add this Leiningen dependency to your project.clj:
```
[clarango "0.3.2"]
```
Then require the lib in your clojure file. For example:
``` Clojure
(:require [clarango.core :as clacore]
			[clarango.database :as database])
```

## Usage

Setting the databse connection and getting a document by existing key:

```clojure
(clarango.core/set-connection! {:connection-url "http://localhost:8529/"})
(clojure.pprint (document/get-by-key "document-key" "my-collection" "my-db"))

;; or

(clarango.core/set-connection! 
  {
    :connection-url "http://localhost:8529/"
    :db-name "my-db"
    :collection-name "my-collection"
    ; if you intend to work with graphs you can optionally add :graph-name "my-graph"
  })
(clojure.pprint (document/get-by-key "document-key"))

;; or

(clarango.core/set-connection! {:connection-url "http://localhost:8529/"})
(clarango.core/set-default-db! "my-db")
(clojure.pprint (document/get-by-key "document-key" "my-collection"))

;; or

;; set default parameters: standart db and port 8529 on localhost
(clarango.core/set-connection!)
(clojure.pprint (document/get-by-key "document-key" "my-collection"))
```

create/replace/update/delete document:

```clojure
(let [_ (clarango.core/set-connection! {
        :connection-url "http://localhost:8529/"
        :db-name "my-db"
        :collection-name "my-collection" })
      document {:name "awesome name" :city "where is he from?"}
      ;; create document
      result-doc (document/create document)
      new-key (get result-doc "_key")]
  (clojure.pprint result-doc)

  ;; replace document
  (let [document-new {:name "even more awesome name" :city "from Berlin of course"}]
    (clojure.pprint (document/replace-by-key document-new new-key)))

  ;; update document
  (let [document-update {:age "He's already 100 years old."}]
    (clojure.pprint (document/update-by-key document-update new-key)))

  ;; delete document
  (document/delete-by-key new-key))
```

## License

Licensed under the [Apache Public License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
