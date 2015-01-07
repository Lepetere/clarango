(ns clarango.index
  (:require [clarango.utilities.http-utility :as http])
  (:use [clarango.utilities.core-utility :only [remove-options-map filter-out-options-map]]
        [clarango.utilities.uri-utility :only [build-resource-uri]]))

(defn get-by-key
  "Returns information about an index.

   First argument: The index key

   Optionally takes a db name as a further argument.
   If omitted, the default db will be used."
  [& args]
  (http/get-uri [:body] (apply build-resource-uri "index" nil (remove-options-map args)) (filter-out-options-map args)))

(defn create
  "Create an index.

  First argument is a map that describes the index properties:
  {'type' type, 'fields' ['field1', 'field2'], ...} (replace the single quotes with double quotes)
  - type is the desired type of the index, e.g. 'hash'
  - fields is a list of fields you want to create the index on
  Please see http://docs.arangodb.org/HttpIndexes/README.html for details, since there are different properties 
  expected for the different kinds of indexes.

  Optionally takes a collection name and a db name as further arguments.
  If omitted, the default db and collection will be used."
  [index & args]
  {:pre [(map? index)]}
  (http/post-uri [:body] (apply build-resource-uri "index/?collection=" nil (remove-options-map args)) index (filter-out-options-map args)))

(defn delete-by-key
  "Deletes an index by its key.

  First argument: The index key

  Optionally takes a db name as a further argument.
  If omitted the default db will be used."
  [& args]
  (http/delete-uri [:body] (apply build-resource-uri "index" nil (remove-options-map args)) (filter-out-options-map args)))

