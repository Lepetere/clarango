(ns clarango.aql
  (:require [clarango.core :as clarango.core]
  			[clarango.utilities.core-utility :as core-utility]
  			[clarango.utilities.aql-utility :as aql-utility]))

(defn explain
  "Validates a query and returns an execution plan for it."
  [query bind-vars]
  nil)

(defn query
  "Validates a query without executing it."
  [query]
  nil)

;; also put cursor handling and function management into this namespace?