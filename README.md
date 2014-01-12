Clarango: a Clojure driver for ArangoDB
========

work in progress... please contact the two repository owners

The expected roadmap is:
* Spring 2014: more sophisticated CRUD and Queries
* Late 2014: stable 1.0 with management / admin, graph, ... and all the features exposed by the REST API.

## Features

* simple Document CRUD
* simple exception handling

...

## Installation

The driver is hosted on [Clojars](https://clojars.org/clarango). Add this Leiningen dependency to your project.clj (no stable release available yet):
```
[clarango "0.0.2-SNAPSHOT"]
```
Then require the lib in your clojure file:
``` Clojure
(:require [clarango.core :as clarango.core]
			[clarango.document :as document])
```

## Usage

Getting a document by existing key:

```clojure
(clarango.core/set-connection! {:connection-url "http://localhost:8529/"})
(clojure.pprint (document/get-by-key "document-key" "my-collection" "my-db"))

;; or

(clarango.core/set-connection! 
  {
    :connection-url "http://localhost:8529/"
    :db-name "my-db"
    :collection-name "my-collection"
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
;; create document
(let [document {:name "awesome name" :city "where is he from?"}
      result-doc (document/create document "my-collection" "my-db")
      new-key (get result-doc "_key")]
  (clojure.pprint result-doc)

  ;; replace document
  (let [document-new {:name "even more awesome name" :city "from Berlin of course"}]
    (clojure.pprint (document/replace-by-key document-new new-key "my-collection" "my-db")))

  ;; update document
  (let [document-update {:age "He's already 100 years old."}]
    (clojure.pprint (document/update-by-key document-update new-key "my-collection" "my-db")))

  ;; delete document
  (clojure.pprint (document/delete-by-key new-key "my-collection" "my-db")))
```

## Feedback

...

## License

Licensed under the [Apache Public License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
