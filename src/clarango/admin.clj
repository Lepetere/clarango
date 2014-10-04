(ns clarango.admin
  (:require [clarango.utilities.http-utility :as http])
  (:use [clarango.utilities.core-utility :only [get-safe-connection-url filter-out-map]]
        [clarango.utilities.uri-utility :only [connect-url-parts]]))

(defn flush
  "The call flushes the modules cache on the server.
  See JSModulesCache for details about this cache.
  No Arguments can be passed.
  This REST call should return '{error false, code 200}'
  with the 200 indicating that the module cache was flushed successfully."
  ; curl http://localhost:8529/_admin/modules/flush
  []
  (http/post-uri [:body] (connect-url-parts (get-safe-connection-url)
                                            "_admin/modules/flush")))

(defn reload
  "Realods the routing information from the collection routing.
  No Arguments can be passed.
  This REST call should return '{error false, code 200}'
  with the 200 indicating that the routing information was reloaded successfully."
  ; curl http://localhost:8529/_admin/routing/reload
  []
  (http/post-uri [:body] (connect-url-parts (get-safe-connection-url)
                                            "_admin/routing/reload")))

(defn statistics
  "Returns a huge Json doc with statistics information.
  Please look up http://www.arangodb.org/manuals/current/HttpSystem#HttpSystemAdminStatistics"
  ; curl http://localhost:8529/_admin/statistics
  []
 (http/post-uri [:body] (connect-url-parts (get-safe-connection-url)
                                           "_admin/statistics")))

(defn stat-desc
 "Returns a description of the statistics returned by /_admin/statistics. The returned objects contains a list of statistics groups in the attribute groups and a list of statistics figures in the attribute figures. For a detailed description please refer to 
http://www.arangodb.org/manuals/current/HttpSystem.html#HttpSystemAdminStatisticsDescription"
  ; curl http://localhost:8529/_admin/statistics-description
  []
  (http/post-uri [:body] (connect-url-parts
                           (get-safe-connection-url)
                           "_admin/statistics-description")))

(defn role
  "New function since Version 2!
  Returns the role of a server in a cluster. The role is returned in the
  role attribute of the result. Possible return values for role are:

  COORDINATOR: the server is a coordinator in a cluster
  PRIMARY: the server is a primary database server in a cluster
  SECONDARY: the server is a secondary database server in a cluster
  UNKNOWN: in a cluster, UNKNOWN is returned if the server role cannot be determined. On a single server, UNKNOWN is the only possible return value."
  ; curl -X GET http://localhost:8529/_admin/server/role => "unknown path" "_admin/server/role"
  []
  (http/get-uri [:body] (connect-url-parts
                          (get-safe-connection-url)
                          "/_admin/server/role")))

(defn log
  "Reads the global log from the server.
  See https://www.arangodb.org/manuals/current/HttpSystem.html#HttpSystemLog
  - upto=[0-4] ;; fatal=0, error =1, warning=2, info=3, debug=4
  - level=[0-4] ;; Returns all log entries of log level level. Use level XOR upto!
  - start=lid ;; Returns all log entries such that their log entry identifier (lid value) is greater or equal to start.
  - size=number ;; Use size and offset for paging
  - offset=number ;; Use size and offset for paging
  - search=textpattern ;; Only return the log entries containing the text pattern
  - sort=[asc|desc] ;; Sort the log entries either ascending (if sort is asc) or descending (if sort is desc) according to their lid values. Note that the lid imposes a chronological order. The default value is asc.
  Example: (admin/log {\"upto\" 4})"
  ; curl -X GET http://localhost:8529/_admin/log?upto=info  => "unknown path" "_admin/server/role"
  [params]
  {:pre [(map? params)]}
  (http/get-uri [:body] (connect-url-parts
                          (get-safe-connection-url)
                          "/_admin/log"
                          (filter-out-map params))))
