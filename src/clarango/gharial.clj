(ns clarango.gharial
  (:require [clarango.utilities.http-utility :as http])
  (:use [clarango.utilities.core-utility :only [remove-options-map filter-out-options-map get-default-graph-name]]
        [clarango.utilities.uri-utility :only [build-resource-uri connect-url-parts]]))

(defn- extract-edge-definitions
  "Extracts from a map the values used when generating multi-collection graphs."
  [edge-def-map]
  (let [from-vertices (apply vector (map #(if (keyword? %) (name %) %) (:from edge-def-map)))
        to-vertices (if (contains? edge-def-map :to) (apply vector (map #(if (keyword? %) (name %) %) (:to edge-def-map))) nil)]
        {
          :collection (:edge-collection edge-def-map)
          :from from-vertices
          :to (if (nil? to-vertices) from-vertices to-vertices)
          :orphanCollections (if (contains? edge-def-map :orphan-collections) (:orphan-collections edge-def-map) [])
          }))

; Create
(defn create
  "Creates a new graph.
  Takes a graph-name and a map or vector of maps containing the edge definitions.
  Each edge definition map should take the following values:
  {
    :edge-collection - A string or keyword providing the name for the graph being created
    :from - A vector of strings or keywords representing existing collection names
    :to - A vector of strings or keywords representing existing collection names (if not provided, the 'from' calloection will be used)
  }"
  [graph-name edge-definition-vector & args]
  {:pre [(or (keyword? graph-name) (string? graph-name)) (vector? edge-definition-vector)]}
  (let [edge-defs (apply vector (map #(extract-edge-definitions %) edge-definition-vector))]
    (http/post-uri [:body "graph"] (apply build-resource-uri "gharial" nil nil (remove-options-map args))
      {:name graph-name :edgeDefinitions edge-defs} (filter-out-options-map args))))

;Get information
(defn get-info
  "Gets info about the Graph
  Returns a map containing info about the graph."
  [graph-name & args]
  {:pre [(or (keyword? graph-name) (string? graph-name))]}
  (http/get-uri [:body "graph"] (apply build-resource-uri "gharial" graph-name nil (remove-options-map args))))

; Delete graph
(defn delete
  "Deletes a graph.
  Also deletes all vertex and edgea collections."
  [graph-name & args]
  {:pre [(or (keyword? graph-name) (string? graph-name))]}
  (http/delete-uri [:body] (apply build-resource-uri "gharial" graph-name nil (remove-options-map args))))

;; Vertices
; Create vertex
(defn create-vertex
  "Creates a new vertex.

  First argument: A map representing the vertex data to be saved.
  If you want to specify a key by yourself, add it as the :_key parameter to the vertex map or use method create-vertex-with-key.
  If you would like the key to be created automatically, just leave this parameter out.

  Second argument: The vertex collection where the vertex should be stored.

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and db will be used.

  Also optional as argument is another map containing further options:
  {'waitForSync' true/false} (replace the single quotes with double quotes)
  - waitForSync meaning if the server response should wait until the vertex is saved to disk;
  The option map might be passed in an arbitrary position after the first argument."
  [vertex vertex-collection & args]
    {:pre [(map? vertex) (or (string? vertex-collection) (keyword? vertex-collection))]}
    (http/post-uri [:body "vertex"] (apply build-resource-uri "gharial" (str "vertex/" (if (keyword? vertex-collection) (name vertex-collection) vertex-collection)) (remove-options-map args)) vertex (filter-out-options-map args)))

; Create vertex with key
(defn create-vertex-with-key
  "Creates a new vertex.

  First argument: A map representing the vertex data to be saved.
  Second argument: The key for the new vertex (string or Clojure keyword)
  Third argument: The vertex collection where the vertex should be stored.

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and db will be used.

  Also optional as argument is another map containing further options:
  {'waitForSync' true/false} (replace the single quotes with double quotes)
  - waitForSync meaning if the server response should wait until the vertex is saved to disk;
  The option map might be passed in an arbitrary position after the first argument."
  [vertex key vertex-collection & args]
    {:pre [(map? vertex) (or (string? vertex-collection) (keyword? vertex-collection))]}
    (http/post-uri [:body "vertex"] (apply build-resource-uri "gharial" (str "vertex/" (if (keyword? vertex-collection) (name vertex-collection) vertex-collection)) (remove-options-map args)) (assoc vertex :_key key) (filter-out-options-map args)))

; Get vertex
(defn get-vertex
  "Gets a vertex.

  Takes the vertex key as the first argument.
  Takes the collection name as the second argument.

  Optionally takes a graph-name and a db name as further arguments.
  If omitted, the default graph and db will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id} (replace the single quotes with double quotes)
  - rev is the document revision; if the current document revision_id does not match the given one, an error is thrown;
  The option map might be passed in an arbitrary position after the first argument."
  [key vertex-collection & args]
  {:pre [(or (keyword? key) (string? key)) (or (string? vertex-collection) (keyword? vertex-collection))]}
  (http/get-uri [:body "vertex"] (apply build-resource-uri "gharial" (connect-url-parts "vertex" (if (keyword? vertex-collection) (name vertex-collection) vertex-collection) key) (remove-options-map args)) (filter-out-options-map args)))

; Replace vertex
(defn replace-vertex
  "Updates a vertex.

  First argument: A map containing the new vertex properties.
  Second argument: The vertex key.
  Third argument: The vertex collection where the vertex resides.

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and db will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id, 'waitForSync' true/false, 'keepNull' true/false} (replace the single quotes with double quotes)
  - rev is the document revision; if the current document revision_id does not match the given one, an error is thrown;
  - waitForSync meaning if the server response should wait until the action was saved to disk;
  - keepNull meaning if the key/value pair should be deleted in the vertex
    if the argument map contains it with a null (nil) as value;
  The option map might be passed in an arbitrary position after the first argument."
  [vertex-properties key vertex-collection & args]
  {:pre [(map? vertex-properties) (or (keyword? key) (string? key)) (or (keyword? vertex-collection) (string? vertex-collection))]}
  (http/put-uri [:body "vertex"] (apply build-resource-uri "gharial" (connect-url-parts "vertex" (if (keyword? vertex-collection) (name vertex-collection) vertex-collection) key) (remove-options-map args)) vertex-properties (filter-out-options-map args)))

; Update Vertex
(defn update-vertex
  "Updates a vertex.

  First argument: A map containing the new vertex properties.
  Second argument: The vertex key.
  Third argument: The vertex collection where the vertex resides.

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and db will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id, 'waitForSync' true/false, 'keepNull' true/false} (replace the single quotes with double quotes)
  - rev is the document revision; if the current document revision_id does not match the given one, an error is thrown;
  - waitForSync meaning if the server response should wait until the action was saved to disk;
  - keepNull meaning if the key/value pair should be deleted in the vertex
    if the argument map contains it with a null (nil) as value;
  The option map might be passed in an arbitrary position after the first argument."
  [vertex-properties key vertex-collection & args]
  {:pre [(map? vertex-properties) (or (keyword? key) (string? key)) (or (keyword? vertex-collection) (string? vertex-collection))]}
  (http/patch-uri [:body "vertex"] (apply build-resource-uri "gharial" (connect-url-parts "vertex" (if (keyword? vertex-collection) (name vertex-collection) vertex-collection) key) (remove-options-map args)) vertex-properties (filter-out-options-map args)))

; Delete Vertex
(defn delete-vertex
  "Deletes a vertex.

  Takes the vertex key as first argument.
  Takes the vertex-collection as the second argument.

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and db will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id, 'waitForSync' true/false} (replace the single quotes with double quotes)
  - rev is the document revision; if the current document revision_id does not match the given one, an error is thrown;
  - waitForSync meaning if the server response should wait until the action was saved to disk;
  The option map might be passed in an arbitrary position after the first argument."
  [key vertex-collection & args]
  {:pre [(or (keyword? key) (string? key)) (or (keyword? vertex-collection) (string? vertex-collection))]}
  (http/delete-uri [:body] (apply build-resource-uri "gharial" (connect-url-parts "vertex" (if (keyword? vertex-collection) (name vertex-collection) vertex-collection) key) (remove-options-map args))))

;; Edges
; Create edges
(defn create-edge
  "Creates a new edge.

  First argument: A map that represents the edge.
  If you want to specify a key by yourself, add it as the :_key parameter to the edge map or use method create-edge-with-key.
  If you would like the key to be created automatically, just leave this parameter out.
  If you optionally want to specify a label for the edge, you can add it as the :$label parameter to the edge map.

  Second argument: The name (_id value) of the from vertex (or just the vertex itself as a map).
  Third argument: The name (_id value) of the to vertex (or just the vertex itself as a map).
  Fourth argument:  The edge collection where the edge is to reside.

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and db will be used.

  Also optional as argument is another map containing further options:
  {'waitForSync' true/false} (replace the single quotes with double quotes)
  - waitForSync meaning if the server response should wait until the edge is saved to disk;
  The option map might be passed in an arbitrary position after the first four arguments."
  [edge vertex-from vertex-to edge-collection & args]
  {:pre [(map? edge) (or (keyword? vertex-from) (string? vertex-from) (map? vertex-from)) (or (keyword? vertex-to) (string? vertex-to) (map? vertex-to))]}
  (let [vertex-from-name (if (map? vertex-from) (get vertex-from "_id") vertex-from)
        vertex-to-name (if (map? vertex-to) (get vertex-to "_id") vertex-to)]
    (http/post-uri [:body "edge"] (apply build-resource-uri "gharial" (str "edge/" (if (keyword? edge-collection) (name edge-collection) edge-collection)) (remove-options-map args))
      (assoc edge "_from" vertex-from-name "_to" vertex-to-name)
      (filter-out-options-map args))))

; Create edge with key
(defn create-edge-with-key
  "Creates a new edge with the provided key.

  First argument: A map that represents the edge.
  Second argument: The key (string or clojure keyword) to be used for the new edge record. If nil then
    a new key will be generated.

  Third argument: The name (_id value) of the from vertex (or just the vertex itself as a map).
  Fourth argument: The name (_id value) of the to vertex (or just the vertex itself as a map).
  Fifth argument:  The edge collection where the edge is to reside.

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and db will be used.

  Also optional as argument is another map containing further options:
  {'waitForSync' true/false} (replace the single quotes with double quotes)
  - waitForSync meaning if the server response should wait until the edge is saved to disk;
  The option map might be passed in an arbitrary position after the first four arguments."
  [edge edge-key vertex-from vertex-to edge-collection & args]
  {:pre [(map? edge) (or (keyword? vertex-from) (string? vertex-from) (map? vertex-from)) (or (keyword? vertex-to) (string? vertex-to) (map? vertex-to))]}
  (let [vertex-from-name (if (map? vertex-from) (get vertex-from "_id") vertex-from)
        vertex-to-name (if (map? vertex-to) (get vertex-to "_id") vertex-to)
        edge-with-or-without-key (if (nil? edge-key) edge (assoc edge :_key edge-key))]
    (http/post-uri [:body "edge"] (apply build-resource-uri "gharial" (str "edge/" (if (keyword? edge-collection) (name edge-collection) edge-collection)) (remove-options-map args))
      (assoc edge-with-or-without-key "_from" vertex-from-name "_to" vertex-to-name)
      (filter-out-options-map args))))

; Get edge
(defn get-edge
  "Gets an edge.

  Takes the edge key as the first argument.
  Takes the edge-collection name as the second argument.

  Optionally takes a graph-name and a db name as further arguments.
  If omitted, the default graph and db will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id} (replace the single quotes with double quotes)
  - rev is the document revision; if the current document revision_id does not match the given one, an error is thrown;
  The option map might be passed in an arbitrary position after the first argument."
  [key edge-collection & args]
  {:pre [(or (keyword? key) (string? key)) (or (string? edge-collection) (keyword? edge-collection))]}
  (http/get-uri [:body "edge"] (apply build-resource-uri "gharial" (connect-url-parts "edge" (if (keyword? edge-collection) (name edge-collection) edge-collection) key) (remove-options-map args)) (filter-out-options-map args)))

; Replace edge
(defn replace-edge
  "Updates a edge.

  First argument: A map containing the new edge properties.
  Second argument: The edge key.
  Third argument: The edge collection where the edge resides.

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and db will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id, 'waitForSync' true/false, 'keepNull' true/false} (replace the single quotes with double quotes)
  - rev is the document revision; if the current document revision_id does not match the given one, an error is thrown;
  - waitForSync meaning if the server response should wait until the action was saved to disk;
  - keepNull meaning if the key/value pair should be deleted in the vertex
    if the argument map contains it with a null (nil) as value;
  The option map might be passed in an arbitrary position after the first argument."
  [edge-properties key edge-collection & args]
  {:pre [(map? edge-properties) (or (keyword? key) (string? key)) (or (keyword? edge-collection) (string? edge-collection))]}
  (http/put-uri [:body "edge"] (apply build-resource-uri "gharial" (connect-url-parts "edge" (if (keyword? edge-collection) (name edge-collection) edge-collection) key) (remove-options-map args)) edge-properties (filter-out-options-map args)))

; Update edge
(defn update-edge
  "Updates an edge.

  First argument: A map containing the new edge properties.
  Second argument: The edge key.
  Third argument: The edge collection where the edge resides.

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and db will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id, 'waitForSync' true/false, 'keepNull' true/false} (replace the single quotes with double quotes)
  - rev is the document revision; if the current document revision_id does not match the given one, an error is thrown;
  - waitForSync meaning if the server response should wait until the action was saved to disk;
  - keepNull meaning if the key/value pair should be deleted in the vertex
    if the argument map contains it with a null (nil) as value;
  The option map might be passed in an arbitrary position after the first argument."
  [edge-properties key edge-collection & args]
  {:pre [(map? edge-properties) (or (keyword? key) (string? key)) (or (keyword? edge-collection) (string? edge-collection))]}
  (http/patch-uri [:body "edge"] (apply build-resource-uri "gharial" (connect-url-parts "edge" (if (keyword? edge-collection) (name edge-collection) edge-collection) key) (remove-options-map args)) edge-properties (filter-out-options-map args)))

; Delete edge
(defn delete-edge
  "Deletes an edge.

  Takes the edge key as first argument.
  Takes the edge-collection as the second argument.

  Takes optional a graph name and a db name as further arguments.
  If omitted by user, the default graph and db will be used.

  Also optional as argument is another map containing further options:
  {'rev' revision_id, 'waitForSync' true/false} (replace the single quotes with double quotes)
  - rev is the document revision; if the current document revision_id does not match the given one, an error is thrown;
  - waitForSync meaning if the server response should wait until the action was saved to disk;
  The option map might be passed in an arbitrary position after the first argument."
  [key edge-collection & args]
  {:pre [(or (keyword? key) (string? key)) (or (keyword? edge-collection) (string? edge-collection))]}
  (http/delete-uri [:body] (apply build-resource-uri "gharial" (connect-url-parts "edge" (if (keyword? edge-collection) (name edge-collection) edge-collection) key) (remove-options-map args))))
