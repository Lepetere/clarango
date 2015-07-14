(ns clarango.core)

(def ^{:dynamic true :private true} clarango-connection nil)

(defn set-connection!
  "Connects permanently to an ArangoDB host by setting the connection map as a global variable.
  If called without arguments set default connection at localhost:8529 with _system db."
  ([]
    (def clarango-connection {:connection-url "http://localhost:8529/", :db-name "_system"}))
  ([connection-map]
    (def clarango-connection connection-map)))

(defn get-connection
  "Returns the db server connection map to other namespaces."
  []
  clarango-connection)

(defn connection-set?
  "Returns true if a connection is set."
  []
  (not (nil? clarango-connection)))

(defn- change-value-in-connection!
  [key value]
  (let [connection-map (if (connection-set?) (get-connection) (hash-map))]
    (set-connection! (assoc connection-map key value))))

(defn set-connection-url!
  "Sets the server url."
  [connection-url]
  (change-value-in-connection! :connection-url connection-url))

(defn set-default-db!
  "Sets a default database."
  [database-name]
  (change-value-in-connection! :db-name database-name))

(defn set-default-collection!
  "Sets a default collection."
  [collection-name]
  (change-value-in-connection! :collection-name collection-name))

(defn set-default-graph!
  "Sets a default graph."
  [graph-name]
  (change-value-in-connection! :graph-name graph-name))

(defmacro with-connection
  "Dynamically rebinds the global connection map.
  Takes a body of code which will be executed in the context of this connection."
  [connection & body]
  `(binding [clarango-connection ~connection]
     ~@body))

(defmacro with-db
  "Dynamically rebinds the default database value.
  Takes a body of code which will be executed in the context of this database."
  [database-name & body]
  `(binding [clarango-connection (assoc (get-connection) :db-name ~database-name)]
     ~@body))

(defmacro with-collection
  "Dynamically rebinds the default collection value.
  Takes a body of code which will be executed in the context of this collection."
  [collection-name & body]
  `(binding [clarango-connection (assoc (get-connection) :collection-name ~collection-name)]
     ~@body))

(defmacro with-graph
  "Dynamically rebinds the default graph value.
  Takes a body of code which will be executed in the context of this graph."
  [graph-name & body]
  `(binding [clarango-connection (assoc (get-connection) :graph-name ~graph-name)]
     ~@body))