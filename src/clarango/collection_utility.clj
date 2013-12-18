(ns clarango.collection-utility
  (:require [clarango.core :as clarango.core]))

(defn ^:private build-collection-URI
  "Build a URI to access a collection in ArangoDB. Uses the defaults set in the clarango-connection 
  for the parts of the URI that are not specified by arguments."
  ;; this is what a URI should look like coming out of this method:
  ;; http://localhost:8529/_db/_system/_api/document/persons/23478695
  ([] nil)
  ([collection-id] nil)
  ([db-name collection-id] nil)
  ;; TO DO: throw exception in every case where a global conn-url is not set (or does not start with 'http://'?)
  )