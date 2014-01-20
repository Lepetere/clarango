(ns clarango.graph
  (:require [clarango.core :as clarango.core]
  			[clarango.utilities.core-utility :as core-utility]))

(defn get-info-by-name
  "Returns information about a graph.

  Takes the graph name as first argument. 

  Optionally takes a database name and a map containing further options as arguments.
  If the database name is omitted, the default database will be used."
  [graph-name &args]
  nil)

(defn get-graphs-info
  "Returns a list of available graphs.

  Can be called without arguments. 
  Optionally takes a database name and a map containing further options as arguments.
  If the database name is omitted, the default database will be used."
  [& args]
  nil)

(defn create
  "Creates a new graph.

  Takes as first three arguments the names of the new graph, the existing vertices collection and the existing edge collection.

  Optionally takes a database name and a map containing further options as arguments.
  If the database name is omitted, the default database will be used."
  [graph-name vertices-collection edge-collection & args]
  nil)

(defn delete
  "Deletes a graph and it's whole set of edges and vertices.

  Takes the graph name as first argument. 

  Optionally takes a database name and a map containing further options as arguments.
  If the database name is omitted, the default database will be used."
  [graph-name &args]
  nil)

(defn execute-traversal
  "Sends a traversal to the server to execute.

  Takes as first three arguments the names of the new graph, the existing vertices collection and the existing edge collection.

  Optionally takes a database name and a map containing further options as arguments.
  If the database name is omitted, the default database will be used.

  By default, all vertices in the graph are visited. Configure the traversal differently by passing options 
  in the option map. You can define filter options and pass a custom JavaScript visitor function here. 
  For details see: http://www.arangodb.org/manuals/current/HttpTraversals.html"
  [start-vertex edge-collection &args]
  nil)