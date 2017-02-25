(ns imb-deps.cytoscape
  (:require [cheshire.core :refer [generate-string]]
            [imb-deps.manual-data :refer [manual-edges]]
            [imb-deps.utils :refer [edges->nodes]]
            [imb-deps.csv :refer [netstat-csvs->edges]]
            [imb-deps.netstat :refer [netstat-raw->edges]]))

(defn convert-to-cytoscape-json [[source target]]
  {:data {:id (rand-int 10000000)
          :source source
          :target target}})

; requires format of [[node1 node2] [node2 node3] ...]
(defn edges->clojure-cytoscape [edges]
  (let [nodes (edges->nodes edges)
        edges (mapv convert-to-cytoscape-json edges)
        nodes (mapv #(let [k %] {:data {:id k}}) nodes)]
    (apply conj edges nodes)))

(defn data->cytoscape []
  (let [old-data manual-edges
        csv-data (netstat-csvs->edges)
        netstat-raw-data (netstat-raw->edges)
        combined-data (reduce #(apply conj %1 %2) [] [old-data csv-data netstat-raw-data])]
    (edges->clojure-cytoscape combined-data)))

(defn generate-cytoscape-json []
  (spit "src/graph-cytoscape/cytoscape.json" (generate-string (data->cytoscape) {:pretty true})))