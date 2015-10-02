(ns clarango.transaction
    (:require [clarango.utilities.http-utility :as http])
    (:use [clarango.utilities.core-utility :only [remove-options-map filter-out-options-map filter-out-collection-name filter-out-database-name]]
          [clarango.utilities.uri-utility :only [build-resource-uri]]))

(defn execute
    "Posts a given defined function to run on ArangoDB in a transaction.

    First argument: A string or vector of strings representing the collection(s)
    used as part of the transaction. Alternatively this can be a map with collection(s)
    scoped as either :read or :write (write defined by default)
    Second argument: The action (JS code) to be run as part of the transaction.

    Response is a map containing the following:
    error: boolean representing whether an error occured
    code: HTTP code for the transaction (200 for success, 400, 409 or 500 otherwise)
    result: the result returned from the transaction (optional field)

    in the event of an error, the following is returned instead of result:
    errorNum: the server error number (optional field)
    errorMessage: the server error message (optional filed)"
    [collections action]
    {:pre [(some true? [(string? collections) (vector? collections) (map? collections)]) (string? action)]}
    (http/post-uri [:body] (apply build-resource-uri "transaction" nil)
        {:collections (if (or (string? collections) (vector? collections))
            {:write collections} collections) :action action}))
