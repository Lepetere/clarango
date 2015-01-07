(ns clarango.database
  (:require [clarango.utilities.http-utility :as http])
  (:use [clarango.utilities.core-utility :only [remove-map filter-out-map]]
        [clarango.utilities.uri-utility :only [build-resource-uri]]))

(defn get-collection-info-list
  "Returns information about all collections in a database as a list.

  Can be called without arguments. In that case the default database will be used.
  Optionally you can pass a database and a map with options as arguments.
  Possible options in the options map are:
  {'excludeSystem' true/false}
  - excludeSystem meaning whether or not the system collections should be excluded from the result."
  [& args]
  (http/get-uri [:body "collections"] (apply build-resource-uri "collection" nil nil (remove-map args)) (filter-out-map args)))

(defn collection-exists?
  "Returns true if a collection with the given name exists, otherwise returns false.

  Can be called without arguments. In that case the default database will be used.
  Optionally you can pass a database name as second argument."
  [collection-name & args]
  (contains? (set (map #(get % "name") (apply get-collection-info-list args))) collection-name))

(defn get-all-graphs
  "Gets a list of all existing graphs within the database.

  Can be called without arguments. In that case the default database will be used.
  Optionally you can pass a database name as argument."
  [& args]
  (http/get-uri [:body "graphs"] (apply build-resource-uri "gharial" nil nil (remove-map args))))

(defn graph-exists?
  "Returns true if a graph with the given name exists, otherwise returns false.

  Can be called without arguments. In that case the default database will be used.
  Optionally you can pass a database name as second argument."
  [graph-name & args]
  (contains? (set (map #(get % "_key") (apply get-all-graphs args))) graph-name))

(defn create
  "Creates a new database.

  First argument: the name of the new database
  Second argument: a vector specifying users to initially create for the new database; 
    can be empty; in this case a default user 'root' with an empty password will be created;
    if not empty, it must contain user objects which may contain the following options:
      - username: the user name as a string
      - passwd: the user password as a string; if omitted, an empty password will be set
      - active: boolean flag indicating whether the user accout should be actived or not; default is true;
      - extra: an optional map of user information that will be saved, but not interpreted by ArangoDB"
  [database-name users]
  {:pre [(or (keyword? database-name) (string? database-name)) (vector? users)]}
  (http/post-uri [:body] (build-resource-uri "database" nil nil "_system") {:name database-name :users users} nil {:database-name database-name}))

(defn delete
  "Deletes a database.

  Expects the database name of the database to be dropped as argument."
  [database-name]
  {:pre [(or (keyword? database-name) (string? database-name))]}
  (http/delete-uri [:body] (build-resource-uri "database" database-name nil "_system")))

(defn get-info-current
  "Returns information about the current database."
  []
  (http/get-uri [:body "result"] (build-resource-uri "database" "current" nil "_system")))

(defn get-info-list
  "Returns a list of all existing databases."
  []
  (http/get-uri [:body "result"] (build-resource-uri "database" nil nil "_system")))

(defn database-exists?
  "Returns true if a database with the given name exists, otherwise returns false."
  [database-name]
  (contains? (set (get-info-list)) database-name))

(defn get-info-user
  "Returns a list of all databases the current user can access.

  Note: this might not work under Windows."
  []
  (http/get-uri [:body "result"] (build-resource-uri "database" "user" nil "_system")))