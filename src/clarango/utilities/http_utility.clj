;; Please note:
;; The methods in this namespace are only intended for internal use.

(ns clarango.utilities.http-utility
	(:require [clj-http.client :as http]
		        [cheshire.core :refer :all])
  (:use clojure.pprint))

;;; debug switches:
(defn debugging-activated? []
  "Switch that activates the verbose output of clj-http."
  false)

(defn console-output-activated? []
  "Switch that activates outputting the http method and url used for each http request."
  true)

(defn- build-server-exception-string
  [error]
  (str "Clarango: There was an error trying to access a resource: " 
    (.getMessage error) 
    "; body: " 
    (parse-string (:body (:object (.getData error))))))

(defn- build-connection-exception-string
  [error]
  (str (.getMessage error)
    "; There is probably something wrong with the server. "
    "Is the server running and did you set the right connection-url?"))

(defmulti handle-error
  "Handle an error trying to access a resource."
  (fn [error] (type error)))
(defmethod handle-error java.net.ConnectException
  [error]
  (throw (Exception. (build-connection-exception-string error))))
(defmethod handle-error clojure.lang.ExceptionInfo
  [error]
  (throw (Exception. (build-server-exception-string error))))
(defmethod handle-error :default
  [error]
  (throw error))
;; TO DO: later create custom exceptions for Clarango?

(defn- get-uppercase-string-for-http-method
  "Returns a string of uppercase letters with the name of the matching http method.
  Pass it a method name as symbol, e.g. :post"
  [method]
  (case method
    :get "GET"
    :head "HEAD"
    :post "POST"
    :put "PUT"
    :patch "PATCH"
    :delete "DELETE"))

(defn- parse-if-possible
  "Parses a JSON string using the cheshire/parse-string function if possible. 
  If an error occurs, the string is returned as it is."
  [string]
  (try (parse-string string)
    (catch Exception e (do #_(println "parse error:   " string) string))))

(defn- incremental-keyword-lookup
  "Takes a map and an array of keywords and performs a nested lookup, meaning one keyword after another is used."
  [map keyword-vec]
  (loop [new-map map keywords keyword-vec]
    (if (empty? keywords) new-map (recur (parse-if-possible (get new-map (first keywords))) (rest keywords)))))

(defn- filter-response
  "Filters a HTTP response with given instruction. Also applies cheshires parse-string method where possible.

  Pass the response JSON as first argument.
  The second argument has to be a map with of the form:
  {:parse-string true/false :keywords [:keyword1 :keyword2...]}"
  [response filter-keys]
  (incremental-keyword-lookup response filter-keys))

(defn- send-request [method response-keys uri body params]
  (if (console-output-activated?) (println (get-uppercase-string-for-http-method method) " connection address: " uri))
  (try (let [ map-with-body (if (nil? body) {} {:body (generate-string body)})
              response (http/request (merge {:method method :url uri :debug (debugging-activated?) :query-params params} map-with-body))]
            (filter-response response response-keys))
        (catch Exception e (handle-error e))))

(defn get-uri 
  ([response-keys uri]
  (send-request :get response-keys uri nil nil))
  ([response-keys uri params]
  (send-request :get response-keys uri nil params)))

(defn head-uri 
  ([response-keys uri]
  (send-request :head response-keys uri nil nil))
  ([response-keys uri params]
  (send-request :head response-keys uri nil params)))

(defn delete-uri 
  ([response-keys uri]
  (send-request :delete response-keys uri nil nil))
  ([response-keys uri params]
  (send-request :delete response-keys uri nil params)))

(defn post-uri 
  ([response-keys uri body]
  (send-request :post response-keys uri body nil))
  ([response-keys uri body params]
  (send-request :post response-keys uri body params)))

(defn put-uri 
  ([response-keys uri body]
  (send-request :put response-keys uri body nil))
  ([response-keys uri body params]
  (send-request :put response-keys uri body params)))

(defn patch-uri 
  ([response-keys uri body]
  (send-request :patch response-keys uri body nil))
  ([response-keys uri body params]
  (send-request :patch response-keys uri body params)))