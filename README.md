Clarango: a Clojure driver for ArangoDB
========

Clarango is a library to connect Clojure with the database [ArangoDB](http://www.arangodb.com/). Although it is work in progress, the parts which are already there are stable. 

The latest version of Clarango on [Clojars](https://clojars.org/clarango) is `0.7.0`.

The compatibility is as follows:
* `0.7.0` is only fully compatible with ArangoDB version `2.6` (and possibly upwards; latest test was with version `2.6.8`).
* `0.6.0` is compatible with ArangoDB `2.3` and `2.4` (sorry, ArangoDB `2.5` is untested; to see what changed with version 0.6, check out [this blog post](http://www.peterfessel.com/2015/01/from-clojure-to-arangodb-clarango-v0-6-released/)).
* `0.5.0` is compatible with ArangoDB versions `1.4` to `2.2`.

## Features

* various options for connecting
* document CRUD including various options
* querying by example
* AQL queries
* collection management
* database management
* graph functions
* index managing
* experimental clojure idiomatic collection methods like `cla-assoc!` and `cla-conj!`
* simple exception handling

## Documentation

For an overview of the features and how to use see below. For more detailed documentation have a look at:
* The [Clarango API overview](http://edlich.github.io/clarango/doc/index.html)
* or the [API overview on crossclj](http://crossclj.info/ns/clarango/latest/clarango.core.html) (possibly older version)
* our book as [pdf](https://leanpub.com/clarango) for printing / download or as [html readable online](https://leanpub.com/clarango/read) (possibly older version)

## Installation

The driver is hosted on [Clojars](https://clojars.org/clarango). Add this Leiningen dependency to your project.clj:
```
[clarango "0.7.0"]
```
Then require the lib in your clojure file. For example:
``` Clojure
(:require [clarango.core :as clacore]
          [clarango.document :as document]
          [clarango.collection :as collection]
          [clarango.gharial :as gharial])
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
    ; if you are working with graphs you can optionally add :graph-name "my-graph"
  })

;; change default db
(clacore/set-default-db! "my-db")

;; ... or just
(clacore/with-connection {:connection-url "http://localhost:8529/" ...}
  ;; ... do your business here
  )
```

The server url is mandatory. Default database and collection are optional.

## Document CRUD

```clojure
(collection/create "test-collection")
(with-collection "test-collection"
  ;; create
  (document/create-with-key {:description "some test document"} :test-doc)
  ;; read
  (document/get-by-key :test-doc)
  (document/get-by-example {:description "some test document"})
  ;; update
  (document/update-by-key {:additional "some additional info"} :test-doc)
  ;; delete
  (document/delete-by-key :test-doc))

```

## Graph

```clojure
(collection/create "people" {"type" 2})
(collection/create "content" {"type" 2})
(gharial/create :test-graph [{:edge-collection "my-test-edges" :from [:people] :to [:content]}])
(with-graph :test-graph
  (gharial/create-vertex {:name "Rich Hickey"} :people))
```

All methods will use the default database and collection unless the names of different ones are passed as optional arguments. For a complete list of methods see the [API overview](http://edlich.github.io/clarango/doc/index.html)

## Bugs / Contributors

If you find bugs or are missing a feature open an issue or feel free to pull request. Furthermore we have easy and hard [open issues](https://github.com/edlich/clarango/issues). So if you like to help us, contact us or / and pick an issue. Also check out [contributions.md](https://github.com/edlich/clarango/blob/master/contributions.md). 

We are looking for contributors to keep the project running, so please get in touch or just pull!

## License

Licensed under the [Apache Public License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
