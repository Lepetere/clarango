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

(defn execute
  "Executes a query.

  First argument must be the query string to be executed.

  If the query references any bind variables, you must additionally pass these in a map as the second argument like this:
  { 'id' 3 } (replace the single quotes with double quotes)
  If you don't use any variables, you can leave this out.

  Optionally you can pass a database name as third (or second) argument. If omitted, the default db will be used.

  The actual result of the query will be contained in the attribute 'result' as a vector.
  For more options see the method execute-count."
  [query-string & args]
    (let [body {"query" query-string}
        bind-vars (filter-out-map args)
        body-bind-vars (if (nil? bind-vars) body (merge body {"bindVars" bind-vars}))]
      (http/post-uri [:body] (apply build-ressource-uri "cursor" nil nil (remove-map args)) body-bind-vars)))

(defn execute-count
  "Executes a query. Takes also the options 'batch-size' and 'count'.

  First argument must be the query string to be executed.

  Second argument must be the batch size. This is the amount of documents that will be returned in the first answer of the
  server. In case there are more documents, in the server answer there will be the attribute 'hasMore' set to true. 
  In this case you can then use the returned cursor 'id' with the method get-more-results to get the remaining results.

  Third argument is 'count', a boolean flag indicating whether or not the number of documents that were found for the query 
  should be included in the result of the query as 'count' attribute. This is turned off by default because it might have 
  an influence on the performance of the query.

  If the query references any bind variables, you must additionally pass these in a map as the fourth argument like this:
  { 'id' 3 } (replace the single quotes with double quotes)
  If you don't use any variables, you can leave this out.

  Optionally you can pass a database name as fifth or fourth argument. If omitted, the default db will be used.

  The actual result of the query will be contained in the attribute 'result' as a vector."
  [query-string batch-size count & args]
    (let [body {"query" query-string, "batchSize" batch-size, "count" count}
        bind-vars (filter-out-map args)
        body-bind-vars (if (nil? bind-vars) body (merge body {"bindVars" bind-vars}))]
      (http/post-uri [:body] (apply build-ressource-uri "cursor" nil nil (remove-map args)) body-bind-vars)))

(defn get-more-results
  "This method gets the remaining results of a query. More results to a query are available if the return value of the
  execute method contained an attribute 'hasMore' set to true.
  If after the execution of this method there are still more results to the query, the return value of this method will
  also contain an attribute 'hasMore' that is set to true.

  Takes as first argument the id of the cursor that was returned by the execute method.

  Optionally you can pass a database name. If omitted, the default db will be used."
  [cursor-id & args]
  (http/put-uri [:body] (apply build-ressource-uri "cursor" cursor-id nil args)))

(defn delete-cursor
  "This method deletes a cursor on the server.
  If you don't intend to make further use of a cursor, you should always delete it to free resources on the server.
  If all available documents of the query were already retrieved by the client, the cursor was already destroyed automatically.

  Takes as first argument the id of the cursor to be deleted. 
  The id was returned by the execute and the get-more-results method.

  Optionally you can pass a database name. If omitted, the default db will be used."
  [cursor-id & args]
  (http/delete-uri [:body] (apply build-ressource-uri "cursor" cursor-id nil args)))