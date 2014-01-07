;; Please note:
;; The methods in this namespace are only intended for internal use.

(ns clarango.core-utility
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
;; TO DO: create custom exceptions for Clarango?
;; TO DO: later put exception handling into it's own namespace?

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

(defn post-uri [uri body params]
  (println "POST connection address: " uri)
  (try (let [opts {:debug (debugging-activated?) :form-params params :body (generate-string body)}
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

(defn get-connection-url
  "Gets the globally in clarango.core set connection url. If no url is set, an exception is thrown."
  []
  (if (clarango.core/connection-set?) 
	(:connection-url (clarango.core/get-connection)) 
	(throw (Exception. "No host URL set."))))

(defn get-safe-connection-url
  "Gets a safe connection url from clarango.core. If it does not start with 'http://', an exception is thrown."
  []
  (let [conn-url (get-connection-url)]
    (if (.startsWith conn-url "http://") 
      conn-url 
      (throw (Exception. "Somethings wrong with the URL. Does not start with http.")))))

(defn get-default-db
  "Gets the globally in clarango.core set db name. If none is set, an exception is thrown."
  []
  (if (clarango.core/connection-set?)
    (let [db-name (:db-name (clarango.core/get-connection))]
      (if (nil? db-name) (throw (Exception. "No default database set.")) db-name))
        (throw (Exception. "No connection set."))))

(defn get-default-collection-name
  "Gets the globally in clarango.core set collection id. If none is set, an exception is thrown."
  []
  (if (clarango.core/connection-set?) 
    (let [collection-name (:collection-name (clarango.core/get-connection))]
      (if (nil? collection-name) (throw (Exception. "No default collection set.")) collection-name)) 
        (throw (Exception. "No connection set."))))

(defn connect-url-parts
  "Builds a string out of different parts. Adds a '/' at the end of each string part if not present."
  [& parts]
    (reduce 
      (fn [base-string add-string]
        (let [url-string (str base-string (clojure.string/trim add-string))] 
          (if (.endsWith add-string "/") url-string (str url-string "/"))))
            "" parts))