(ns imb-deps.lacij-graphs
  (:require [lacij.edit.graph :refer [build graph add-node add-edge add-default-node-attrs add-default-node-style]]
            [lacij.view.graphview :refer [export]]
            [lacij.layouts.layout :refer [layout]]
            [imb-deps.utils :refer [edges->nodes]]
            [imb-deps.manual-data :refer [manual-edges]]))

(defn add-nodes [g nodes]
  (reduce (fn [g node]
            (add-node g node (name node)))
    g
    nodes))

(defn add-edges [g edges]
  (reduce (fn [g [src dst]]
            (let [id (keyword (str (name src) "-" (name dst)))]
              (add-edge g id src dst)))
    g
    edges))

(defn gen-graph3 []
  (-> (graph :width 1600 :height 1200)
    (add-default-node-attrs :width 500 :height 500 :shape :circle)
    (add-default-node-style :r 30)
    (add-nodes (edges->nodes manual-edges))
    (add-edges manual-edges)))

(defn generate-lacij-graph []
  (let [g (-> (gen-graph3)
            (layout :radial :radius 600 :flow :out)
            (build))]
    (export g "/Users/ken/Desktop/flow-radial.svg" :indent "yes")))