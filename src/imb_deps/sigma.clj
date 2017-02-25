(ns imb-deps.sigma
  (:require [cheshire.core :refer [generate-string]]
            [imb-deps.manual-data :refer [manual-edges]]
            [imb-deps.utils :refer [edges->nodes]]
            [imb-deps.csv :refer [netstat-csvs->edges]]
            [imb-deps.netstat :refer [netstat-raw->edges]]))

(defn convert-to-sigma-json [[source target]]
  {:id (str (rand-int 10000000))
   :source source
   :target target})

; requires format of [[node1 node2] [node2 node3] ...]
(defn edges->clojure-sigma [edges]
  (let [edges (mapv convert-to-sigma-json edges)]
    edges))

(defn get-color [server-name]
  (cond
    (re-matches #".*sql.*" (str server-name)) "#FFC107"
    (re-matches #".*app.*" (str server-name)) "#4CAF50"
    (re-matches #".*web.*" (str server-name)) "#03A9F4"
    (re-matches #".*eset.*" (str server-name)) "#F44336"
    :else "#000"))

(defn data->sigma []
  (let [old-data manual-edges
        csv-data (netstat-csvs->edges)
        netstat-raw-data (netstat-raw->edges)
        combined-edges (reduce #(apply conj %1 %2) [] [old-data csv-data netstat-raw-data])
        nodes (mapv #(let [k %] {:id k :label k :x (rand-int 2000) :y (rand-int 2000) :size 3 :color (get-color k)}) (edges->nodes combined-edges))
        edges (edges->clojure-sigma combined-edges)]
    {:nodes nodes
     :edges edges}))

(defn generate-sigma-json []
  (spit "src/graph-sigma/build/sigma.json" (generate-string (data->sigma) {:pretty true})))