;; Please note:
;; The methods in this namespace are only intended for internal use.

(ns clarango.http-utility
	(:require [clj-http.client :as http]
		        [cheshire.core :refer :all])
  (:use clojure.pprint))

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

(defn debugging-activated? []
  false)

(defn get-uri [uri]
  (println "GET connection address: " uri)
  (try (let [opts {:debug (debugging-activated?)}
              response (http/request (merge {:method :get :url uri} opts))]
        (parse-string (:body response)))
        (catch Exception e (handle-error e))))

(defn head-uri [uri params]
  (println "HEAD connection address: " uri)
  (try (let [opts {:debug (debugging-activated?)}
              response (http/request (merge {:method :head :url uri} opts))]
        (parse-string (:body response)))
        (catch Exception e (handle-error e))))

(defn post-uri [uri body params] ; TO DO: make params optional
  (println "POST connection address: " uri)
  (try (let [_ (println (generate-string body))
    opts {:debug (debugging-activated?) :form-params params :body (generate-string body)}
              response (http/request (merge {:method :post :url uri} opts))]
        (parse-string (:body response)))
        (catch Exception e (handle-error e))))

(defn put-uri [uri body params]
  (println "PUT connection address: " uri)
  (try (let [opts {:debug (debugging-activated?)}
              response (http/request (merge {:method :put :url uri} opts))]
        (parse-string (:body response)))
        (catch Exception e (handle-error e))))

(defn patch-uri [uri body params]
  (println "PATCH connection address: " uri)
  (try (let [opts {:debug (debugging-activated?)}
              response (http/request (merge {:method :patch :url uri} opts))]
        (parse-string (:body response)))
        (catch Exception e (handle-error e))))

(defn delete-uri [uri params]
  (println "DELETE connection address: " uri)
  (try (let [opts {:debug (debugging-activated?)}
              response (http/request (merge {:method :delete :url uri} opts))]
        (parse-string (:body response)))
        (catch Exception e (handle-error e))))