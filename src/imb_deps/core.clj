(ns imb-deps.core
  (:require [imb-deps.lacij-graphs :refer [generate-lacij-graph]]
            [imb-deps.csv :as csv]
            [imb-deps.netstat :as netstat]
            [imb-deps.cytoscape :as cytoscape]
            [imb-deps.sigma :as sigma]))

(defn -main []
  (sigma/generate-sigma-json)
  #_(cytoscape/generate-cytoscape-json)
  #_(csv/netstat-csvs->edges)
  #_(generate-lacij-graph))