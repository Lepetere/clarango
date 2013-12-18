Clarango: a Clojure driver for ArangoDB
========

work in progress... please contact the two repository owners

The expected roadmap is:
* Jan 2014: Simple Read, Write, Update, Delete.
* Spring 2014: more sophisticated CRUD and Queries
* Late 2014: stable 1.0 with management / admin, graph, ... and all the features exposed by the REST API.

## Features

...

## Installation

The driver is hosted on [Clojars](https://clojars.org/clarango). Add this Leiningen dependency to your project.clj (no stable release available yet):
```
[clarango "0.0.2-SNAPSHOT"]
```
Then require the lib in your clojure file:
``` Clojure
(:require [clarango.core :as yourshortcut])
```

## Usage

Getting a document by an existing key:

```clojure
(clarango.core/connect! 
  {
    :connection-url  "http://localhost:8529/"
  })
(let [result (document/get-by-key "my-db" "my-collection" "document-key")]
      (clojure.pprint result))
```

or

```clojure
(clarango.core/connect! 
  {
    :connection-url  "http://localhost:8529/"
    :db-name "my-db"
    :collection-name "my-collection"
  })
(let [result (document/get-by-key "document-key")]
      (clojure.pprint result))
```

or

```clojure
(clarango.core/connect! {:connection-url  "http://localhost:8529/"})
(clarango.core/set-default-db! "my-db")
(let [result (document/get-by-key "my-collection" "document-key")]
      (clojure.pprint result))
```

## Feedback

...

## License

Licensed under the [Apache Public License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
