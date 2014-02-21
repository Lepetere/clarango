(ns clarango.database
  (:require [clarango.core :as clarango.core]
            [clarango.utilities.core-utility :as core-utility]
            [clarango.utilities.uri-utility :as uri-utility]
            [clarango.utilities.http-utility :as http]))

(defn get-collection-info-list
  "Returns information about all collections in a database as a list.

  Can be called without arguments. In that case the default database will be used.
  Optionally you can pass a database and a map with options as arguments.
  Possible options in the options map are:
  {'excludeSystem' true/false}
  - excludeSystem meaning whether or not the system collections should be excluded from the result."
  [& args]
  (http/get-uri [:body "collections"] (apply uri-utility/build-ressource-uri "collection" nil nil (core-utility/remove-map args)) (core-utility/filter-out-map args)))

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
  (http/post-uri [:body] (uri-utility/build-ressource-uri "database" nil nil "_system") {:name database-name :users users}))

(defn delete
  "Deletes a database.

  Expects the database name of the database to be dropped as argument."
  [database-name]
  (http/delete-uri [:body] (uri-utility/build-ressource-uri "database" database-name nil "_system")))

(defn get-info-current
  "Returns information about the current database."
  []
  (http/get-uri [:body "result"] (uri-utility/build-ressource-uri "database" "current" nil "_system")))

(defn get-info-list
  "Returns a list of all existing databases."
  []
  (http/get-uri [:body "result"] (uri-utility/build-ressource-uri "database" nil nil "_system")))

(defn get-info-user
  "Returns a list of all databases the current user can access.

  Note: this might not work under Windows."
  []
  (http/get-uri [:body "result"] (uri-utility/build-ressource-uri "database" "user" nil "_system")))