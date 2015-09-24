(ns clarango.function
    (:require [clarango.utilities.http-utility :as http])
    (:use [clarango.utilities.core-utility :only [remove-options-map filter-out-options-map filter-out-collection-name filter-out-database-name]]
          [clarango.utilities.uri-utility :only [build-resource-uri]]))
;; POST function
(defn new-function
    "Create a new user defined function.

    First argument: A string representing the new function name
    (e.g. 'myfunctions::temperature::celsiustofahrenheit')
    Second argument: A stringified JavaScript function representing the code to be run
    (e.g. 'function (celsius) { return celsius * 1.8 + 32; }')

    Respoonse codes are as follows:
    HTTP 200: Function was found and replaced by this call.
    HTTP 201: Function did not exist but was created.
    HTTP 400: Malformed request."
    [name code & args]
    {:pre [(string? name) (string? code)]}
    (http/post-uri [:body] (apply build-resource-uri "aqlfunction" nil)
        {:name name :code code}))

;; get
(defn get-functions
    "Returns all user defined functions in the database.

    Will return a JSON array with all user defined functions in the format:
    [
        {
            \"name\" : \"myfunctions::temperature::celsiustofahrenheit\",
            \"code\" : \"function (celsius) { return celsius * 1.8 + 32; }\"
        }
    ]"
    [& args]
    (http/get-uri [:body] (apply build-resource-uri "aqlfunction" nil)))

;; delete
(defn delete-function
    "Deletes a user defined function with the given name.

    First argument: A string representing the full user defined function name.

    Response codes are as follows:
    HTTP 200: The function will be removed by the server.
    HTTP 400: The request is badly formed.
    HTTP 404: The specified function was not found."
    [function-name & args]
    (http/delete-uri [:body] (apply build-resource-uri (format "aqlfunction/%s" function-name) nil)))
