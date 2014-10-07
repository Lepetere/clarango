(ns clarango.graph
  (:require [clarango.utilities.http-utility :as http])
  (:use [clarango.utilities.core-utility :only [remove-map filter-out-map]]
        [clarango.utilities.uri-utility :only [build-resource-uri connect-url-parts]]))

(defn execute-traversal
  "Sends a traversal to the server to execute it. Output are vertices and edges.

  First argument: The key of the start vertex.
  Second argument: The name of the collection that contains the vertices.
  Third argument: The name of the collection that contains the edges.
  Fourth argument: The direction of the traversal. Must be either 'outbound', 'inbound' or 'any'. 
  Can be nil if the 'expander' attribute is set in the additional options.

  Takes optionally a database name as further argument.
  If omitted by user, the default database will be used.

  Also optional as argument is another map containing further options for the traversal:
  {'filter' {...}, 'expander' code}
  - see http://www.arangodb.org/manuals/current/HttpTraversals.html#HttpTraversalsPost

  The option map might be passed in an arbitrary position after the first four arguments."
  [start-vertex vertex-collection edges-collection direction & args]
  {:pre [(or (keyword? start-vertex) (string? start-vertex)) (or (keyword? vertex-collection) (string? vertex-collection)) (or (keyword? edges-collection) (string? edges-collection)) (or (nil? direction) (string? direction))]}
  (let [body {"startVertex" (str vertex-collection "/" start-vertex) "edgeCollection" edges-collection}
        body-with-direction (if (nil? direction) body (assoc body "direction" direction))]
    (http/post-uri [:body "result" "visited"] (apply build-resource-uri "traversal" nil nil (remove-map args)) 
        body-with-direction
        (filter-out-map args))))

(defn execute-vertex-traversal
  "Executes a traversal on vertices only.
  Depending on the batch size returns a cursor with the vertex data.

  First argument: The key of the start vertex.
  Second argument: The batch size of the returned cursor.
  Third argument: The result size.
  Fourth argument: A boolean value which determines if the result should return a property 'count' with the total amount of traversed vertices.
  Fifth argument: An optional filter for the results. If you don't want to use it, just pass nil here.
  For details on the filter see http://www.arangodb.org/manuals/current/HttpGraph.html#A_JSF_POST_graph_vertices

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and db will be used."
  [key batch-size limit count filter & args]
  {:pre [(or (keyword? key) (string? key)) (number? batch-size) (number? limit) (= (type count) java.lang.Boolean) (or (map? filter) (nil? filter))]}
  (let [body {"batchSize" batch-size "limit" limit "count" count}
        body-with-filter (if (nil? filter) body (assoc body "filter" filter))]
    (http/post-uri [:body] (apply build-resource-uri "graph" (connect-url-parts "vertices" key) (remove-map args)) 
      body-with-filter
      (filter-out-map args))))

(defn execute-edge-traversal
  "Executes a traversal on edges only.
  Depending on batch size returns a cursor with the edge data.

  First argument: The key of the start edge.
  Second argument: The batch size of the returned cursor.
  Third argument: The result size.
  Fourth argument: ???
  Fifth argument: An optional filter for the results. If you don't want to use it, just pass nil here.
  For details on the filter see http://www.arangodb.org/manuals/current/HttpGraph.html#A_JSF_POST_graph_edges

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and db will be used."
  [key batch-size limit count filter & args]
  {:pre [(or (keyword? key) (string? key)) (number? batch-size) (number? limit) (= (type count) java.lang.Boolean) (or (map? filter) (nil? filter))]}
  (let [body {"batchSize" batch-size "limit" limit "count" count}
        body-with-filter (if (nil? filter) body (assoc body "filter" filter))]
    (http/post-uri [:body] (apply build-resource-uri "graph" (connect-url-parts "edges" key) (remove-map args)) 
      body-with-filter
      (filter-out-map args))))

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
  {:pre [(or (keyword? graph-name) (string? graph-name)) (or (keyword? vertices-collection) (string? vertices-collection)) (or (keyword? edges-collection) (string? edges-collection))]}
  (http/post-uri [:body "graph"] (apply build-resource-uri "graph" nil nil (remove-map args)) 
    {"_key" graph-name, "vertices" vertices-collection, "edges" edges-collection} 
    (filter-out-map args)))

(defn get-info
  "Gets info about a graph.
  Returns a map containing information about the graph.

  Takes the name of the graph as first argument.

  Optionally you can pass a database name as second argument. If omitted, the default db will be used."
  [graph-name & args]
  {:pre [(or (keyword? graph-name) (string? graph-name))]}
  (http/get-uri [:body "graph"] (apply build-resource-uri "graph" graph-name nil (remove-map args))))

(defn delete
  "Deletes a graph.
  Also deletes it's vertex and the edges collection.

  Takes the name of the graph as first argument.

  Optionally you can pass a database name as second argument. If omitted, the default db will be used."
  [graph-name & args]
  {:pre [(or (keyword? graph-name) (string? graph-name))]}
  (http/delete-uri [:body] (apply build-resource-uri "graph" graph-name nil (remove-map args))))

(defn create-vertex
  "Creates a vertex. 

  First argument: A map that represents the vertex. 
  If you want to specify a key by yourself, add it as the :_key parameter to the vertex map or use method create-vertex-with-key. 
  If you would like the key to be created automatically, just leave this parameter out.

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and db will be used.

  Also optional as argument is another map containing further options:
  {'waitForSync' true/false} (replace the single quotes with double quotes)
  - waitForSync meaning if the server response should wait until the vertex is saved to disk;
  The option map might be passed in an arbitrary position after the first argument."
  [vertex & args]
  {:pre [(map? vertex)]}
  (http/post-uri [:body "vertex"] (apply build-resource-uri "graph" "vertex" (remove-map args)) vertex (filter-out-map args)))

(defn create-vertex-with-key
  "Creates a vertex with a given key.

  First argument: A map that represents the vertex. 
  Second argument: The key for the new vertex (string or clojure keyword).

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and db will be used.

  Also optional as argument is another map containing further options:
  {'waitForSync' true/false} (replace the single quotes with double quotes)
  - waitForSync meaning if the server response should wait until the vertex is saved to disk;
  The option map might be passed in an arbitrary position after the first argument."
  [vertex key & args]
  {:pre [(map? vertex) (or (keyword? key) (string? key))]}
  (http/post-uri [:body "vertex"] (apply build-resource-uri "graph" "vertex" (remove-map args)) (assoc vertex :_key key) (filter-out-map args)))

(defn get-vertex
  "Gets a vertex.

  Takes the vertex key as first argument. 

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and db will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id} (replace the single quotes with double quotes)
  - rev is the document revision; if the current document revision_id does not match the given one, an error is thrown;
  The option map might be passed in an arbitrary position after the first argument."
  [key & args]
  {:pre [(or (keyword? key) (string? key))]}
  (http/get-uri [:body "vertex"] (apply build-resource-uri "graph" (connect-url-parts "vertex" key) (remove-map args)) (filter-out-map args)))

(defn replace-vertex
  "Replaces a vertex.

  First argument: A map containing the new vertex.
  Second argument: The vertex key.

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and db will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id, 'waitForSync' true/false} (replace the single quotes with double quotes)
  - rev is the document revision; if the current document revision_id does not match the given one, an error is thrown;
  - waitForSync meaning if the server response should wait until the action was saved to disk;
  The option map might be passed in an arbitrary position after the first argument."
  [vertex-properties key & args]
  {:pre [(map? vertex-properties) (or (keyword? key) (string? key))]}
  (http/put-uri [:body "vertex"] (apply build-resource-uri "graph" (connect-url-parts "vertex" key) (remove-map args)) vertex-properties (filter-out-map args)))

(defn update-vertex
  "Updates a vertex.

  First argument: A map containing the new vertex properties.
  Second argument: The vertex key.

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and db will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id, 'waitForSync' true/false, 'keepNull' true/false} (replace the single quotes with double quotes)
  - rev is the document revision; if the current document revision_id does not match the given one, an error is thrown;
  - waitForSync meaning if the server response should wait until the action was saved to disk;
  - keepNull meaning if the key/value pair should be deleted in the vertex
    if the argument map contains it with a null (nil) as value;
  The option map might be passed in an arbitrary position after the first argument."
  [vertex-properties key & args]
  {:pre [(map? vertex-properties) (or (keyword? key) (string? key))]}
  (http/patch-uri [:body "vertex"] (apply build-resource-uri "graph" (connect-url-parts "vertex" key) (remove-map args)) vertex-properties (filter-out-map args)))

(defn delete-vertex
  "Deletes a vertex.

  Takes the vertex key as first argument. 

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and db will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id, 'waitForSync' true/false} (replace the single quotes with double quotes)
  - rev is the document revision; if the current document revision_id does not match the given one, an error is thrown;
  - waitForSync meaning if the server response should wait until the action was saved to disk;
  The option map might be passed in an arbitrary position after the first argument."
  [key & args]
  {:pre [(or (keyword? key) (string? key))]}
  (http/delete-uri [:body] (apply build-resource-uri "graph" (connect-url-parts "vertex" key) (remove-map args)) (filter-out-map args)))

(defn create-edge
  "Creates a new edge.

  First argument: A map that represents the edge.
  If you want to specify a key by yourself, add it as the :_key parameter to the edge map or use method create-edge-with-key. 
  If you would like the key to be created automatically, just leave this parameter out.
  If you optionally want to specify a label for the edge, you can add it as the :$label parameter to the edge map.

  Second argument: The name of the from vertex.
  Third argument: The name of the to vertex.

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and db will be used.

  Also optional as argument is another map containing further options:
  {'waitForSync' true/false} (replace the single quotes with double quotes)
  - waitForSync meaning if the server response should wait until the edge is saved to disk;
  The option map might be passed in an arbitrary position after the first four arguments."
  [edge vertex-from-name vertex-to-name & args]
  {:pre [(map? edge) (or (keyword? vertex-from-name) (string? vertex-from-name)) (or (keyword? vertex-to-name) (string? vertex-to-name))]}
  (http/post-uri [:body "edge"] (apply build-resource-uri "graph" "edge" (remove-map args)) 
    (assoc edge "_from" vertex-from-name "_to" vertex-to-name) 
    (filter-out-map args)))

(defn create-edge-with-key
  "Creates a new edge with a given key.

  First argument: A map that represents the edge.
  Second argument: The key for the new edge (string or clojure keyword). If you pass nil, a key will be generated automatically.
  Third argument: A label for the new edge. If you don't want to set one, just pass nil.
  Fourth argument: The name of the from vertex.
  Fifth argument: The name of the to vertex.

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and db will be used.

  Also optional as argument is another map containing further options:
  {'waitForSync' true/false} (replace the single quotes with double quotes)
  - waitForSync meaning if the server response should wait until the edge is saved to disk;
  The option map might be passed in an arbitrary position after the first four arguments."
  [edge key label vertex-from-name vertex-to-name & args]
  {:pre [(map? edge) (or (nil? key) (or (keyword? key) (string? key))) (or (nil? label) (or (keyword? label) (string? label))) (or (keyword? vertex-from-name) (string? vertex-from-name)) (or (keyword? vertex-to-name) (string? vertex-to-name))]}
  (http/post-uri [:body "edge"] (apply build-resource-uri "graph" "edge" (remove-map args)) 
    (let [edge-with-or-without-key (if (nil? key) edge (assoc edge :_key key))
          edge-with-or-without-label (if (nil? label) edge-with-or-without-key (assoc edge-with-or-without-key :$label label))]
      (assoc edge-with-or-without-label "_from" vertex-from-name "_to" vertex-to-name))
    (filter-out-map args)))

(defn get-edge
  "Gets an edge.

  Takes the edge key as first argument. 

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and db will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id} (replace the single quotes with double quotes)
  - rev is the document revision; if the current document revision_id does not match the given one, an error is thrown;
  The option map might be passed in an arbitrary position after the first argument."
  [key & args]
  {:pre [(or (keyword? key) (string? key))]}
  (http/get-uri [:body "edge"] (apply build-resource-uri "graph" (connect-url-parts "edge" key) (remove-map args)) (filter-out-map args)))

(defn replace-edge
  "Replaces an edge.

  First argument: A map containing the new edge.
  Second argument: The edge key.

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and db will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id, 'waitForSync' true/false} (replace the single quotes with double quotes)
  - rev is the document revision; if the current document revision_id does not match the given one, an error is thrown;
  - waitForSync meaning if the server response should wait until the action was saved to disk;
  The option map might be passed in an arbitrary position after the first argument."
  [edge-properties key & args]
  {:pre [(map? edge-properties) (or (keyword? key) (string? key))]}
  (http/put-uri [:body "edge"] (apply build-resource-uri "graph" (connect-url-parts "edge" key) (remove-map args)) edge-properties (filter-out-map args)))

(defn update-edge
  "Updates an edge.

  First argument: A map containing the new edge properties.
  Second argument: The edge key.

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and db will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id, 'waitForSync' true/false, 'keepNull' true/false} (replace the single quotes with double quotes)
  - rev is the document revision; if the current document revision_id does not match the given one, an error is thrown;
  - waitForSync meaning if the server response should wait until the action was saved to disk;
  - keepNull meaning if the key/value pair should be deleted in the edge
    if the argument map contains it with a null (nil) as value;
  The option map might be passed in an arbitrary position after the first argument."
  [edge-properties key & args]
  {:pre [(map? edge-properties) (or (keyword? key) (string? key))]}
  (http/patch-uri [:body "edge"] (apply build-resource-uri "graph" (connect-url-parts "edge" key) (remove-map args)) edge-properties (filter-out-map args)))

(defn delete-edge
  "Deletes an edge.

  Takes the edge key as first argument. 

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and db will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id, 'waitForSync' true/false} (replace the single quotes with double quotes)
  - rev is the document revision; if the current document revision_id does not match the given one, an error is thrown;
  - waitForSync meaning if the server response should wait until the action was saved to disk;
  The option map might be passed in an arbitrary position after the first argument."
  [key & args]
  {:pre [(or (keyword? key) (string? key))]}
  (http/delete-uri [:body] (apply build-resource-uri "graph" (connect-url-parts "edge" key) (remove-map args)) (filter-out-map args)))