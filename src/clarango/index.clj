(ns clarango.index
  (:require [clarango.utilities.http-utility :as http])
  (:use [clarango.utilities.core-utility :only [remove-map filter-out-map]]
        [clarango.utilities.uri-utility :only [build-resource-uri]]))

(defn get-by-key
  "Returns information about an index

   Takes the index key as its first argument.

   Optionally takes a db name as a further argument.
   If omitted, the default db will be used"
  [& args]
  (http/get-uri [:body] (apply build-resource-uri "index" nil (remove-map args)) (filter-out-map args)))

(defn create
  "Create an index.

  First argument: A map that represents the index
  Optionally takes a collection name and a db name as further arguments.
  If omitted, the default db and collection will be used"
  [index & args]
  (http/post-uri [:body] (apply build-resource-uri "index/?collection=" nil (remove-map args)) index (filter-out-map args)))

(defn delete-by-key
  "Deletes an index by its id.

  First argument: the index id
  Optionally takes a db name as a further argument.
  If omitted the default db will be used"
  [& args]
  (http/delete-uri [:body] (apply build-resource-uri "index" nil (remove-map args)) (filter-out-map args)))

