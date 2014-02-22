(ns clarango.graph
  (:require [clarango.core :as clarango.core]
            [clarango.utilities.http-utility :as http])
  (:use [clarango.utilities.core-utility :only [remove-map filter-out-map]]
        [clarango.utilities.uri-utility :only [build-ressource-uri]]))

(defn execute-traversal
  "Sends a traversal to the server to execute it.

  Takes as first argument the document handle of the vertex to start the traversal.

  Takes optionally a collection name and a db name as further arguments.
  The collection must be an edge collection to perform the traversal on.
  If omitted by user, the default db and collection will be used.

  Also optional as argument is another map containing further options for the traversal:
  {}
  - ... see http://www.arangodb.org/manuals/current/HttpTraversals.html#HttpTraversalsPost
  The option map might be passed in an arbitrary position after the first two arguments."
  [start-vertex & args]
  nil)

(defn create
  "Creates a new graph.

  First argument: The name of the graph to be created.
  Second argument: The name of the collection containing the vertices.
  Third argument: The name of the collection containing the edges.
  The ladder two collections must already exist.

  Optionally you can pass a database name as fourth argument. If omitted, the default db will be used.

  Also optional as argument is another map containing further options:
  {'waitForSync' true/false} (replace the single quotes with double quotes)
  - waitForSync meaning if the server response should wait until the graph has been to disk;"
  [graph-name vertices-collection edges-collection & args]
  (http/post-uri [:body "graph"] (apply build-ressource-uri "graph" nil nil (remove-map args)) 
    {"_key" graph-name, "vertices" vertices-collection, "edges" edges-collection} 
    (filter-out-map args)))

(defn get-info
  "Gets info about a graph.
  Returns a map containing information about the graph.

  Takes the name of the graph as first argument.

  Optionally you can pass a database name as second argument. If omitted, the default db will be used."
  [graph-name & args]
  (http/get-uri [:body "graph"] (apply build-ressource-uri "graph" graph-name nil (remove-map args))))

(defn delete
  "Deletes a graph.
  Also deletes it's vertex and the edges collection.

  Takes the name of the graph as first argument.

  Optionally you can pass a database name as second argument. If omitted, the default db will be used."
  [graph-name & args]
  (http/delete-uri [:body] (apply build-ressource-uri "graph" graph-name nil (remove-map args))))

(defn create-vertex
  "Creates a new vertex."
  []
  nil)

(defn get-vertex
  "Gets a vertex."
  []
  nil)

(defn replace-vertex
  "Replaces a vertex."
  []
  nil)

(defn update-vertex
  "Updates a vertex."
  []
  nil)

(defn delete-vertex
  "Deletes a vertex."
  []
  nil)

(defn get-vertices
  "Gets several vertices.
  Depending on batch size returns a cursor."
  [batch-size limit count filter]
  nil)

(defn create-edge
  "Creates a new edge."
  []
  nil)

(defn get-edge
  "Gets an edge."
  []
  nil)

(defn replace-edge
  "Replaces an edge."
  []
  nil)

(defn update-edge
  "Updates an edge."
  []
  nil)

(defn delete-edge
  "Deletes an edge."
  []
  nil)

(defn get-edges
  "Gets several edges.
  Depending on batch size returns a cursor."
  [batch-size limit count filter]
  nil)