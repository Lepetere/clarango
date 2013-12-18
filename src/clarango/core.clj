(ns clarango.core )

(def clarango-connection nil)

(defn connect!
  "Connects permanently to an ArangoDB host by setting the connection map as a global variable."
  [connection-map]
  ;; TODO: test if url is string and starts with http://
  ;; TODO: test if there is a database server available
  (def clarango-connection connection-map)

  ;;; This is what the connection map should look like:
  ;; {
  ;; :conn-url  myconnurl, ;; hat url and ports 
  ;; :db-name mydbname ;; kann fehlen und ggf. per Arg
  ;; :collection-name ;; kann fehlen und ggf. per Argument angegeben werden
  ;; }
  )

(defn get-connection
  "Returns the db server connection map to other namespaces."
  []
  clarango-connection
  )

(defn connection-set?
  "Returns true if a connection is set."
  []
  (not (nil? clarango-connection)))

(defn create-database
  "Creates a database with the given name."
  [database-name]
  nil)

(defn get-database
  "Returns a reference to a given database."
  [database-name]
  nil)

(defn ^:private change-value-in-connection!
  [key value]
  (let [connection-map (if (connection-set?) (get-connection) (hash-map))]
    (connect! (assoc connection-map key value))))

(defn set-default-db!
  "Sets a default database."
  [database-name]
  (change-value-in-connection! :db-name database-name))

(defn set-default-collection!
  "Sets a default collection."
  [collection-name]
  (change-value-in-connection! :collection-name collection-name))