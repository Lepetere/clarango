(ns clarango.user
  (:require [clarango.utilities.http-utility :as http])
  (:use [clarango.utilities.core-utility :only [remove-options-map]]
        [clarango.utilities.uri-utility :only [build-resource-uri]]))

;; Get
(defn get-all
  "Fetches data about all users currently defined in the system"
  []
  (http/get-uri [:body] (apply build-resource-uri "user" nil)))

(defn get-by-username
  "Fetches data about the specified user."
  [username]
    {:pre [(string? username)]}
    (http/get-uri [:body] (apply build-resource-uri (format "user/%s" username) nil)))

;; Create
(defn create
  "The following data needs to be passed in the {document} parameter as a map:

  - user: The name of the user as a string. This is mandatory
  - passwd: The user password as a string. If no password is specified, the empty string will be used
  - active: An optional flag that specifies whether the user is active. If not specified, this will default to true
  - extra: An optional JSON object with arbitrary extra data about the user
  - changePassword: An optional flag that specifies whethers the user must change the password or not. If not specified, this will default to false."
  [document]
  {:pre [(map? document)]}
  (http/post-uri [:body] (apply build-resource-uri "user" nil) document))

;; Update / PATCH
(defn replace-by-username
  "Replaces the data of an existing user. The name of an existing user must be specified for the {username} parameter.

  The following data can be passed in the {document} parameter as a map:

  - passwd: The user password as a string. Specifying a password is mandatory, but the empty string is allowed for passwords
  - active: An optional flag that specifies whether the user is active. If not specified, this will default to true
  - extra: An optional JSON object with arbitrary extra data about the user
  - changePassword: An optional flag that specifies whether the user must change the password or not. If not specified, this will default to false"
  [document username]
  {:pre [(map? document) (string? username)]}
  (http/put-uri [:body] (apply build-resource-uri (format "user/%s" username) nil) document))

(defn update-by-username
  "Partially updates the data of an existing user. The name of an existing user must be specified for the {username} parameter.

  The following data can be passed in the {document} parameter as a map:

  - passwd: The user password as a string. Specifying a password is optional. If not specified, the previously existing value will not be modified.
  - active: An optional flag that specifies whether the user is active. If not specified, the previously existing value will not be modified.
  - extra: An optional JSON object with arbitrary extra data about the user. If not specified, the previously existing value will not be modified.
  - changePassword: An optional flag that specifies whether the user must change the password or not."
  [document username]
  {:pre [(map? document) (string? username)]}
  (http/patch-uri [:body] (apply build-resource-uri (format "user/%s" username) nil) document))

;; Delete
(defn delete-by-username
  "Removes an existing user, identified by the {username} parameter."
  [username]
    {:pre [(string? username)]}
    (http/delete-uri [:body] (apply build-resource-uri (format "user/%s" username) nil)))

;; Exists can go in database namespace
