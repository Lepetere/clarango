;; Please note:
;; The methods in this namespace are only intended for internal use.

(ns clarango.document-utility
  (:use [clarango.core-utility :only [connect-url-parts get-safe-connection-url get-default-db get-default-collection-name]]))

(defn build-document-uri
  "Build a URI to access a collection in ArangoDB. Uses the defaults set in the clarango-connection 
  for the parts of the URI that are not specified by arguments."
  ([document-key]
  	(connect-url-parts (get-safe-connection-url) "_db/" (get-default-db) "_api/document/" (get-default-collection-name) document-key))
  ([document-key collection-name]
  	(connect-url-parts (get-safe-connection-url) "_db/" (get-default-db) "_api/document/" collection-name document-key))
  ([document-key collection-name db-name]
  	(connect-url-parts (get-safe-connection-url) "_db/" db-name "_api/document/" collection-name document-key)))