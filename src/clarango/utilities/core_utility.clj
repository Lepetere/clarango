;; Please note:
;; The methods in this namespace are only intended for internal use.

(ns clarango.utilities.core-utility
  (:require [clarango.core :as clarango.core]))

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
  "Gets the globally in clarango.core set collection name. If none is set, an exception is thrown."
  []
  (if (clarango.core/connection-set?) 
    (let [collection-name (:collection-name (clarango.core/get-connection))]
      (if (nil? collection-name) (throw (Exception. "No default collection set.")) collection-name)) 
        (throw (Exception. "No connection set."))))

(defn get-default-graph-name
  "Gets the globally in clarango.core set graph name. If none is set, an exception is thrown."
  []
  (if (clarango.core/connection-set?) 
    (let [graph-name (:graph-name (clarango.core/get-connection))]
      (if (nil? graph-name) (throw (Exception. "No default graph set.")) graph-name)) 
        (throw (Exception. "No connection set."))))

(defn remove-options-map
  "Takes a vector and returns it without the elements that are of type map. Only removes maps that do not
  contain the keywords :database-name or :collection-name in their metadata."
  [vect]
  (remove #(if (= (type %) clojure.lang.PersistentArrayMap) (not (or (contains? (meta %) :database-name) (contains? (meta %) :collection-name))) false) vect))

(defn filter-out-options-map
  "Returns the first element in a vector that is of type map, except for maps that contain the keywords 
  :database-name or :collection-name."
  [vect]
  (first (filter #(if (= (type %) clojure.lang.PersistentArrayMap) (not (or (contains? (meta %) :database-name) (contains? (meta %) :collection-name))) false) vect)))

(defn filter-out-collection-name
  "Filters out the collection name of the additional argument vector of the clojure API methods.
  Returns the default collection, if not present in the argument vector."
  [args]
  (let [args-without-map (remove-options-map args)]
    (case (count args-without-map)
      0 (get-default-collection-name)
      1 (nth args-without-map 0)
      2 (nth args-without-map 0))))

(defn filter-out-database-name
  "Filters out the database name of the additional argument vector of the clojure API methods.
  Returns the default database, if not present in the argument vector."
  [args]
  (let [args-without-map (remove-options-map args)]
    (case (count args-without-map)
      0 (get-default-db)
      1 (get-default-db)
      2 (nth args-without-map 1))))