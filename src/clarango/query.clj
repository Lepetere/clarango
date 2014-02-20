(ns clarango.query
  (:require [clarango.core :as clarango.core]
            [clarango.utilities.http-utility :as http])
  (:use [clarango.utilities.core-utility :only [remove-map filter-out-map]]
        [clarango.utilities.uri-utility :only [build-ressource-uri]]))

(defn explain
  "Explains how a query would be executed on the server. Returns an execution plan for the query.

  First argument must be the query string to be evaluated.
  If the query references any bind variables, you must pass these in a map as second argument like this:
  { 'id' 3 } (replace the single quotes with double quotes)
  If you don't use any variables, you can leave the second argument out.

  Optionally you can pass a database name as third or second argument. If omitted, the default db will be used."
  [query-string & args]
  	(let [body {"query" query-string}
  		  bind-vars (filter-out-map args)
  		  body-bind-vars (if (nil? bind-vars) body (merge body {"bindVars" bind-vars}))]
  	  (http/post-uri [:body "plan"] (apply build-ressource-uri "explain" nil nil (remove-map args)) body-bind-vars)))

(defn validate
  "Validates a query without executing it.
  As a return value you get a map containing the names of the collections and the vars used in the query.
  If the query is not valid also an error will be thrown including an error message with the problem found in the query.

  Takes as only argument the query string to be evaluated."
  [query-string]
  (http/post-uri [:body] (build-ressource-uri "query") {"query" query-string}))