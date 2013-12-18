(ns clarango.collection-utility
  (:require [clarango.core :as clarango.core]))

(defn ^:private build-collection-URI-using-defaults 
  "Build a URI to access a collection in ArangoDB. Uses the defaults set in the clarango-connection 
  for the parts of the URI that are not specified by arguments."
  ([] )
  ([root-url] )
  ([root-url db-name] )
  ([root-url db-name collection-id]))