(ns clarango.query
  (:require [clarango.core :as clarango.core]
            [clarango.utilities.http-utility :as http])
  (:use [clarango.utilities.core-utility :only [remove-map filter-out-map filter-out-collection-name-from-args filter-out-database-name-from-args]]
        [clarango.utilities.uri-utility :only [build-ressource-uri]]))

(defn explain
  "Explains how a query would be executed on the server. Returns an execution plan for the query.

  First argument must be the query string to be evaluated.
  If the query references any bind variables, you must pass these in a map as second argument like this:
  { 'id' 3 } (replace the single quotes with double quotes)
  If you don't use any variables, you can leave the second argument out."
  ([query-string]
  	(http/post-uri [:body "plan"] (build-ressource-uri "explain") {"query" query-string}))
  ([query-string bind-vars]
  	(http/post-uri [:body "plan"] (build-ressource-uri "explain") {"query" query-string, "bindVars" bind-vars})))

(defn validate
  "Validates a query without executing it.
  As a return value you get a map containing the names of the collections and the vars used in the query.
  If the query is not valid also an error will be thrown including an error message with the problem found in the query.

  Takes as only argument the query string to be evaluated."
  [query-string]
  (http/post-uri [:body] (build-ressource-uri "query") {"query" query-string}))