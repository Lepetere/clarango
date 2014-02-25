(ns clarango.graph
  (:require [clarango.core :as clarango.core]
            [clarango.utilities.http-utility :as http])
  (:use [clarango.utilities.core-utility :only [remove-map filter-out-map]]
        [clarango.utilities.uri-utility :only [build-ressource-uri connect-url-parts]]))

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
  "Creates a vertex. 

  First argument: A map that represents the vertex. 
  If you want to specify a key by yourself, add it as the :_key parameter to the vertex map. 
  If you would like the key to be created automatically, just leave this parameter out.

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and collection will be used.

  Also optional as argument is another map containing further options:
  {'waitForSync' true/false} (replace the single quotes with double quotes)
  - waitForSync meaning if the server response should wait until the vertex is saved to disk;
  The option map might be passed in an arbitrary position after the first argument."
  [vertex & args]
  (http/post-uri [:body "vertex"] (apply build-ressource-uri "graph" "vertex" (remove-map args)) vertex (filter-out-map args)))

(defn get-vertex
  "Gets a vertex.

  Takes the vertex key as first argument. 

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and collection will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id} (replace the single quotes with double quotes)
  - rev is the document revision; if the current document revision_id does not match the given one, an error is thrown;
  The option map might be passed in an arbitrary position after the first argument."
  [key & args]
  (http/get-uri [:body "vertex"] (apply build-ressource-uri "graph" (connect-url-parts "vertex" key) (remove-map args)) (filter-out-map args)))

(defn replace-vertex
  "Replaces a vertex.

  First argument: A map containing the new vertex.
  Second argument: The vertex key.

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and collection will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id, 'waitForSync' true/false} (replace the single quotes with double quotes)
  - rev is the document revision; if the current document revision_id does not match the given one, an error is thrown;
  - waitForSync meaning if the server response should wait until the action was saved to disk;
  The option map might be passed in an arbitrary position after the first argument."
  [vertex-properties key & args]
  (http/put-uri [:body "vertex"] (apply build-ressource-uri "graph" (connect-url-parts "vertex" key) (remove-map args)) vertex-properties (filter-out-map args)))

(defn update-vertex
  "Updates a vertex.

  First argument: A map containing the new vertex properties.
  Second argument: The vertex key.

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and collection will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id, 'waitForSync' true/false} (replace the single quotes with double quotes)
  - rev is the document revision; if the current document revision_id does not match the given one, an error is thrown;
  - waitForSync meaning if the server response should wait until the action was saved to disk;
  The option map might be passed in an arbitrary position after the first argument."
  [vertex-properties key & args]
  (http/patch-uri [:body "vertex"] (apply build-ressource-uri "graph" (connect-url-parts "vertex" key) (remove-map args)) vertex-properties (filter-out-map args)))

(defn delete-vertex
  "Deletes a vertex.

  Takes the vertex key as first argument. 

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and collection will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id, 'waitForSync' true/false} (replace the single quotes with double quotes)
  - rev is the document revision; if the current document revision_id does not match the given one, an error is thrown;
  - waitForSync meaning if the server response should wait until the action was saved to disk;
  The option map might be passed in an arbitrary position after the first argument."
  [key & args]
  (http/delete-uri [:body] (apply build-ressource-uri "graph" (connect-url-parts "vertex" key) (remove-map args)) (filter-out-map args)))

(defn get-vertices
  "Gets several vertices.
  Depending on batch size returns a cursor."
  [batch-size limit count filter]
  nil)

(defn create-edge
  "Creates a new edge.

  First argument: A map that represents the edge.
  If you optionally want to specify a label for the edge, you can add it as the :$label parameter to the edge map.

  Second argument: The name of the edge to be created.
  Third argument: The name of the from vertex.
  Fourth argument: The name of the to vertex.

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and collection will be used.

  Also optional as argument is another map containing further options:
  {'waitForSync' true/false} (replace the single quotes with double quotes)
  - waitForSync meaning if the server response should wait until the edge is saved to disk;
  The option map might be passed in an arbitrary position after the first four arguments."
  [edge edge-name vertex-from-name vertex-to-name & args]
  ;; what about the document key if the user desires to specify it by himself? 
  ;; Should he just pass it in the json document? or allow it as optional argument?
  (http/post-uri [:body "edge"] (apply build-ressource-uri "graph" "edge" (remove-map args)) 
    (assoc edge "_key" edge-name "_from" vertex-from-name "_to" vertex-to-name) 
    (filter-out-map args)))

(defn get-edge
  "Gets an edge.

  Takes the edge key as first argument. 

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and collection will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id} (replace the single quotes with double quotes)
  - rev is the document revision; if the current document revision_id does not match the given one, an error is thrown;
  The option map might be passed in an arbitrary position after the first argument."
  [key & args]
  (http/get-uri [:body "edge"] (apply build-ressource-uri "graph" (connect-url-parts "edge" key) (remove-map args)) (filter-out-map args)))

(defn replace-edge
  "Replaces an edge.

  First argument: A map containing the new edge.
  Second argument: The edge key.

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and collection will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id, 'waitForSync' true/false} (replace the single quotes with double quotes)
  - rev is the document revision; if the current document revision_id does not match the given one, an error is thrown;
  - waitForSync meaning if the server response should wait until the action was saved to disk;
  The option map might be passed in an arbitrary position after the first argument."
  [edge-properties key & args]
  (http/put-uri [:body "edge"] (apply build-ressource-uri "graph" (connect-url-parts "edge" key) (remove-map args)) edge-properties (filter-out-map args)))

(defn update-edge
  "Updates an edge.

  First argument: A map containing the new edge properties.
  Second argument: The edge key.

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and collection will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id, 'waitForSync' true/false} (replace the single quotes with double quotes)
  - rev is the document revision; if the current document revision_id does not match the given one, an error is thrown;
  - waitForSync meaning if the server response should wait until the action was saved to disk;
  The option map might be passed in an arbitrary position after the first argument."
  [edge-properties key & args]
  (http/patch-uri [:body "edge"] (apply build-ressource-uri "graph" (connect-url-parts "edge" key) (remove-map args)) edge-properties (filter-out-map args)))

(defn delete-edge
  "Deletes an edge.

  Takes the edge key as first argument. 

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and collection will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id, 'waitForSync' true/false} (replace the single quotes with double quotes)
  - rev is the document revision; if the current document revision_id does not match the given one, an error is thrown;
  - waitForSync meaning if the server response should wait until the action was saved to disk;
  The option map might be passed in an arbitrary position after the first argument."
  [key & args]
  (http/delete-uri [:body] (apply build-ressource-uri "graph" (connect-url-parts "edge" key) (remove-map args)) (filter-out-map args)))

(defn get-edges
  "Gets several edges.
  Depending on batch size returns a cursor."
  [batch-size limit count filter]
  nil)