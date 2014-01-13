(ns clarango.database
  (:require [clarango.core :as clarango.core]
            [clarango.utilities.database-utility :as database-utility]))

(defn get-collection-info-list ; in the ArangoDB REST API this method is part of the Collection API, but is this here not a better place?
  "Returns information about all collections in a database in a list.

  Can be called without arguments. In that case the default database will be used.
  Optionally you can pass a database and a map with options as arguments.
  Possible options in the options map are:
  {'excludeSystem' true/false}
  - excludeSystem meaning"
  [& args]
  nil)

(defn get-current-info
  "Returns information about the current database."
  [] ; no arguments!
  nil)

(defn create
  "Creates a database."
  [database-name]
  nil)

(defn delete
  "Deletes a database."
  [database-name]
  nil)

(defn get-database-list
  "Returns a list of all existing databases."
  [] ; takes no arguments because the _system database has to be used for this request anyway
  nil)

(defn get-user-database-list
  "Returns a list of all databases the current user can access."
  []
  nil)