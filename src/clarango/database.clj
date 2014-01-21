(ns clarango.database
  (:require [clarango.core :as clarango.core]
            [clarango.utilities.core-utility :as core-utility]
            [clarango.utilities.uri-utility :as uri-utility]
            [clarango.utilities.http-utility :as http]))

(defn get-collection-info-list ; in the ArangoDB REST API this method is part of the Collection API, but is this here not a better place?
  "Returns information about all collections in a database in a list.

  Can be called without arguments. In that case the default database will be used.
  Optionally you can pass a database and a map with options as arguments.
  Possible options in the options map are:
  {'excludeSystem' true/false}
  - excludeSystem meaning whether or not the system collections should be excluded from the result."
  [& args]
  (http/get-uri [:body] (apply build-ressource-uri "collection" nil nil (core-utility/remove-map args)) nil (core-utility/filter-out-map args)))

(defn create
  "Creates a database."
  [database-name]
  nil)

(defn delete
  "Deletes a database."
  [database-name]
  nil)

(defn get-info-current
  "Returns information about the current database."
  []
  (http/get-uri [:body "result"] (uri-utility/build-ressource-uri "database" "current" nil "_system")))

(defn get-info-list
  "Returns a list of all existing databases."
  []
  (http/get-uri [:body "result"] (uri-utility/build-ressource-uri "database" nil nil "_system")))

(defn get-info-user
  "Returns a list of all databases the current user can access."
  []
  (http/get-uri [:body "result"] (uri-utility/build-ressource-uri "database" "user" nil "_system")))