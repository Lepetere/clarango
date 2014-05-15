<!-- img src="https://travis-ci.org/edlich/clarango.png?branch=master" alt="travis-ci.org Build Status" title="Build Status" align="right" /-->
Clarango: a Clojure driver for ArangoDB
========

Clarango is a library to connect Clojure with the database [ArangoDB](http://www.arangodb.org/). Although it is work in progress, the parts which are already there are stable. 

The current lib version on clojars is 0.4. The library should work with ArangoDB versions at least from 1.4.0 upwards (latest test was with version 2.0.6).

For an overview of the features and how to use see below. For more detailed documentation also have a look at:
* The [API overview](http://edlich.github.io/clarango/docs/uberdoc.html) can also be found on [crossclj](http://crossclj.info) [here](http://crossclj.info/ns/clarango/0.3.2/clarango.core.html) (possibly older version)
* Some examples can be found [here](https://github.com/edlich/clarango/blob/development/src/clarango/main.clj)
* A book as [pdf](https://leanpub.com/clarango) for printing / download or as [html readable online](https://leanpub.com/clarango/read)

## Features

* various options for connecting (see [core namespace](https://github.com/edlich/clarango/blob/master/src/clarango/core.clj))
* document CRUD including various options (see [document namespace](https://github.com/edlich/clarango/blob/master/src/clarango/document.clj))
* querying by example (see [document namespace](https://github.com/edlich/clarango/blob/master/src/clarango/document.clj))
* AQL queries (see [query namespace](https://github.com/edlich/clarango/blob/master/src/clarango/query.clj))
* collection management (see [collection namespace](https://github.com/edlich/clarango/blob/master/src/clarango/collection.clj))
* database management (see [database namespace](https://github.com/edlich/clarango/blob/master/src/clarango/database.clj))
* graph functions (see [graph namespace](https://github.com/edlich/clarango/blob/master/src/clarango/graph.clj))
* simple exception handling
* experimental clojure idiomatic collection methods like `cla-assoc!` and `cla-conj!` (see [collection-ops namespace](https://github.com/edlich/clarango/blob/master/src/clarango/collection_ops.clj) for details)

## Installation

The driver is hosted on [Clojars](https://clojars.org/clarango). Add this Leiningen dependency to your project.clj:
```
[clarango "0.4"]
```
Then require the lib in your clojure file. For example:
``` Clojure
(:require [clarango.core :as clacore]
          [clarango.document :as document]
          [clarango.collection :as collection])
```

## Setting the Connection

```clojure
;; connect to localhost and default port 8529
(clacore/set-connection!)

;; pass a connection map
(clacore/set-connection! 
  {
    :connection-url "http://localhost:8529/"
    :db-name "my-db"
    :collection-name "my-collection"
    ; if you intend to work with graphs you can optionally add :graph-name "my-graph"
  })

;; change default db
(clacore/set-default-db! "my-db")
```

The server url is mandatory. Default database and collection are optional.

## Document CRUD

```clojure
(collection/create "test-collection")
(with-collection "test-collection"
  ;; create
  (document/create-with-key {:description "some test document"} "test-doc")
  ;; read
  (document/get-by-key "test-doc")
  (document/get-by-example {:description "some test document"})
  ;; update
  (document/update-by-key {:additional "some additional info"} "test-doc")
  ;; delete
  (document/delete-by-key "test-doc"))

```

All methods will use the default database and collection unless the names of different ones are passed as optional arguments. For a complete list of methods see the [API overview](http://edlich.github.io/clarango/docs/uberdoc.html)

## Bugs

If you find bugs or are missing a feature open an issue or feel free to pull request!

If you like it give us a :star:

## License

Licensed under the [Apache Public License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
