(ns clarango.test.gharial
  (:require [clojure.test :refer :all]
            [clarango.core :as cla-core]
            [clarango.collection :as collection]
            [clarango.database :as database]
            [clarango.query :as query]
            [clarango.graph :as graph]
            [clarango.gharial :as gharial]
            [clarango.misc :as misc])
  (:use clojure.pprint)
  (:use clarango.collection-ops)
  (:use clarango.core))

(defn setup []
  (cla-core/set-connection!)
  (database/create "GharialTestDB" []))

(defn teardown []
  (database/delete "GharialTestDB"))

(defn fixture [f]
  (setup)
  (f)
  (teardown))

(use-fixtures :each fixture)

(deftest create-and-delete-single-collection-graph

  (testing "Creating a new single-collection graph returns map of graph-information"
    (with-db "GharialTestDB"
      (collection/create :people {"type" 2})
      (let [create-graph-result (gharial/create :test-graph [{:edge-collection "my-test-edges" :from [:people] :to [:people]}])]
        (is (not (nil? create-graph-result)) true)
        (is (= "test-graph" (create-graph-result "name"))))))

  (testing "Delete new graph"
    (with-db "GharialTestDB"
      (pprint (gharial/delete :test-graph)))))

(deftest create-and-delete-multi-collection-graph

  (testing "Creating a new multi-collection graph returns a map of graph-information"
    (with-db "GharialTestDB"
      (collection/create :people {"type" 2})
      (collection/create :content {"type" 2})
      (let [create-graph-result (gharial/create :test-graph [{:edge-collection "my-test-edges" :from [:people] :to [:content]}])]
        (is (not (nil? create-graph-result)))
        (is (= "test-graph" (create-graph-result "name"))))))

  (testing "Delete new graph returns a map confirming removal w/ status code 200"
    (with-db "GharialTestDB"
      (let [delete-graph-result (gharial/delete :test-graph)]
        (is (true? (delete-graph-result "removed")))
        (is (= 200 (delete-graph-result "code")))))))

(deftest basic-vertex-management

  (with-db "GharialTestDB"
    (collection/create :people {"type" 2})
    (collection/create :content {"type" 2})
    (gharial/create :test-graph [{:edge-collection "my-test-edges" :from [:people] :to [:content]}]))

  (def vertex-save-record (atom nil))

  (testing "Create a new Vertex in 'people' collection returns map containing _key, _rev and _id"
    (with-db "GharialTestDB"
      (with-graph "test-graph"
        (let [vertex-result (gharial/create-vertex {:name "Rich Hickey"} :people)]
          (is (true? (contains? vertex-result "_key")))
          (is (true? (contains? vertex-result "_rev")))
          (is (true? (contains? vertex-result "_id")))
          (reset! vertex-save-record vertex-result)))))

  (testing "Fetching created vertex returns previously created object"
    (with-db "GharialTestDB"
      (with-graph "test-graph"
        (let [vertex-result (gharial/get-vertex (@vertex-save-record "_key") :people)]
          (is (true? (contains? vertex-result "_key")))
          (is (every? true? [(contains? vertex-result "name") (= (vertex-result "name") "Rich Hickey")]))))))

  (testing "Replacing a created vertex returns map containing _key, _id, an updated _rev and the _oldRev value"
    (with-db "GharialTestDB"
      (with-graph "test-graph"
        (let [vertex-result (gharial/replace-vertex {:foo "bar"} (@vertex-save-record "_key")  :people)]
          (is (every? true? [(contains? vertex-result "_key") (contains? vertex-result "_rev") (contains? vertex-result "_id")]))
          (is (true? (= (vertex-result "_oldRev") (@vertex-save-record "_rev"))))
          (reset! vertex-save-record vertex-result)))))

  (testing "Fetching the replaced vertex returns the new model without any previous values"
    (with-db "GharialTestDB"
      (with-graph "test-graph"
        (let [vertex-result (gharial/get-vertex (@vertex-save-record "_key") :people)]
          (is (every? true? [(contains? vertex-result "foo") (= (vertex-result "foo") "bar")]))
          (is (false? (contains? vertex-result "name")))))))

  (testing "Updating a created vertex returns map containing _key, _id, an updated _rev and the oldRev value"
    (with-db "GharialTestDB"
      (with-graph "test-graph"
        (let [vertex-result (gharial/update-vertex {:name "Rich Hickey" :foo "baz"} (@vertex-save-record "_key") :people)]
          (is (every? true? [(contains? vertex-result "_key") (contains? vertex-result "_rev") (contains? vertex-result "_id")]))
          (is (true? (= (vertex-result "_oldRev") (@vertex-save-record "_rev"))))
          (reset! vertex-save-record vertex-result)))))

  (testing "Fetching the updated vertex returns the updated model with new and updated fields"
    (with-db "GharialTestDB"
      (with-graph "test-graph"
        (let [vertex-result (gharial/get-vertex (@vertex-save-record "_key") :people)]
          (is (every? true? [(contains? vertex-result "foo") (= (vertex-result "foo") "baz") ]))
          (is (every? true? [(contains? vertex-result "name") (= (vertex-result "name") "Rich Hickey")]))))))

  (testing "Deleting the vertex returns a map with 'code' => '202' and 'removed' => true"
    (with-db "GharialTestDB"
      (with-graph "test-graph"
        (let [delete-result (gharial/delete-vertex (@vertex-save-record "_key") :people)]
          (is (every? true? [(contains? delete-result "code") (= (delete-result "code") 202)]))
          (is (every? true? [(contains? delete-result "removed") (= (delete-result "removed") true)])))))))

(deftest basic-edge-management

  ; Initialise
  (with-db "GharialTestDB"
    (collection/create :people {"type" 2})
    (collection/create :content {"type" 2})
    (gharial/create :test-graph [{:edge-collection "my-test-edges" :from [:people] :to [:content]}]))

  (def edge-save-result (atom nil))

  (testing "Adding a new edge in 'my-test-edges' collection returns map containing _key, _rev and _id"
    (with-db "GharialTestDB"
      (with-graph "test-graph"
        (let [edge-result (gharial/create-edge {:type "foo"} (gharial/create-vertex {:name "bar"} :people) (gharial/create-vertex {:title "stuff"} :content) :my-test-edges)]
          (is (every? true? [(contains? edge-result "_key") (contains? edge-result "_rev") (contains? edge-result "_id")]))
          (reset! edge-save-result edge-result)))))

  (testing "Fetching created edge returns previously created object"
    (with-db "GharialTestDB"
      (with-graph "test-graph"
        (let [edge-result (gharial/get-edge (@edge-save-result "_key") :my-test-edges)]
          (is (every? true? [(contains? edge-result "_key") (contains? edge-result "type")]))
          (is (true? (= (edge-result "type") "foo")))))))

  (testing "Replacing a created edge returns map containing _key, _rev, an updated _rev and the _oldRev value"
    (with-db "GharialTestDB"
      (with-graph "test-graph"
        (let [edge-result (gharial/replace-edge {:foo "bar"} (@edge-save-result "_key") :my-test-edges)]
          (is (every? true? [(contains? edge-result "_key") (contains? edge-result "_rev") (contains? edge-result "_id") (contains? edge-result "_oldRev")]))
          (is (true? (= (edge-result "_oldRev") (@edge-save-result "_rev"))))
          (reset! edge-save-result edge-result)))))

  (testing "Fetching the replaced edge returns the new model without any previous values"
    (with-db "GharialTestDB"
      (with-graph "test-graph"
        (let [edge-result (gharial/get-edge (@edge-save-result "_key") :my-test-edges)]
          (is (true? (contains? edge-result "foo")))
          (is (true? (= (edge-result "foo") "bar")))
          (is (false? (contains? edge-result "type")))))))

  (testing "updating an existing edge returns a map containing _key, _id, an updated _rev and the old_rev"
    (with-db "GharialTestDB"
      (with-graph "test-graph"
        (let [edge-result (gharial/update-edge {:type "my-type"} (@edge-save-result "_key") :my-test-edges)]
          (is (every? true? [(contains? edge-result "_key") (contains? edge-result "_rev") (contains? edge-result "_id")]))
          (is (true? (= (edge-result "_oldRev") (@edge-save-result "_rev"))))
          (reset! edge-save-result edge-result)))))

  (testing "Fetching the updated vertex returns the updated model with both previous and updated fields"
    (with-db "GharialTestDB"
      (with-graph "test-graph"
        (let [edge-result (gharial/get-edge (@edge-save-result "_key") :my-test-edges)]
          (is (every? true? [(contains? edge-result "foo") (= (edge-result "foo") "bar")]))
          (is (every? true? [(contains? edge-result "type") (= (edge-result "type") "my-type")]))))))

  (testing "Deleting the edge returns a map with 'code' => '202' and 'removed' => true"
    (with-db "GharialTestDB"
      (with-graph "test-graph"
        (let [delete-result (gharial/delete-vertex (@edge-save-result "_key") :my-test-edges)]
          (is (every? true? [(contains? delete-result "code") (= (delete-result "code") 202)]))
          (is (every? true? [(contains? delete-result "removed") (= (delete-result "removed") true)])))))))

(deftest graph-and-query-and-traversal-test

  (testing "Initialising Test Graph"
    (with-db "GharialTestDB"
      (collection/create :people {"type" 2})
      (collection/create :content {"type" 2})
      (gharial/create "test-graph" [{:edge-collection :actions :from [:people] :to [:content]}])
      (is (true? (database/graph-exists? "test-graph")))))

  (testing "Initialising Test Graph Vertices Data"
    (with-db "GharialTestDB"
      (with-graph "test-graph"
        (gharial/create-vertex-with-key {:name "Bob" :age 28} "bob-key" :people)
        (gharial/create-vertex-with-key {:name "Peter" :age 25} "peter-key" :people)
        (gharial/create-vertex-with-key {:title "Some great content"} "great-content-key" :content)
        (gharial/create-vertex-with-key {:title "Some rather 'meh' content"} "meh-content-key" :content))))

  (testing "Initialising Test Graph Edges Data"
    (with-db "GharialTestDB"
      (with-graph "test-graph"
        (gharial/create-edge {:action_type "viewed"} (gharial/get-vertex "bob-key" :people) (gharial/get-vertex "great-content-key" :content) :actions)
        (gharial/create-edge {:action_type "viewed"} (gharial/get-vertex "peter-key" :people) (gharial/get-vertex "great-content-key" :content) :actions)
        (gharial/create-edge {:action_type "liked"} (gharial/get-vertex "bob-key" :people) (gharial/get-vertex "meh-content-key" :content) :actions))))

  (testing "Outbound traversal from Peter returns:
            - a map
            - contains a 'vertices' and a 'path' element
            - vertices stanza has entries for 'Peter' (source) and 'Some great content' (target)"
    (with-db "GharialTestDB"
      (with-graph "test-db"
        (let [traversal-result (graph/execute-traversal "peter-key" "people" "actions" "outbound")]
          (is (map? traversal-result))
          (is (every? true? [(contains? traversal-result "vertices") (contains? traversal-result "paths")]))
          (is (every? true? [(contains? (first (traversal-result "vertices")) "name") (= (get (first (traversal-result "vertices")) "name") "Peter")]))
          (is (every? true? [(contains? (second (traversal-result "vertices")) "title") (= (get (second (traversal-result "vertices")) "title") "Some great content")])))))))
