(ns clarango.test.document
  (:use clojure.test clarango.core)
  (:require [clarango.document :as doc]))

(deftest document-test
  (testing "Simple CRUD on a single document")
  (println "Document testing: Not yet implemented!")
    ;; create db
    ;; create collection
    ;;(set-default-db! "test-db")
    ;; define a document
    ;;(doc/create ...) ;; capture key
    ;;(is (doc/get-info ...)) ;; is created?
    ;;(is (doc/get-by-key ...)) ;; is the same?
    ;;(doc/update-by-key ...))
    ;;(is (doc/get-by-key ...)) ;; has been updated?
    ;;(doc/create ...)) ;; create new document
    ;;(doc/replace-by-key ...)
    ;;(is (doc/get-by-key ...)) ;; is replaced?
    ;;(doc/delete-by-key ...)
    ;;(is (doc/get-info ...)) ;; has been deleted
  )

;; Don't forget *by-example*
