(ns imb-deps.csv
  (:require [clojure.data.csv :as csv]
            [imb-deps.utils :refer [useless-ips empty-row? addr->domain-name filename->keyword edgify]]))

(def base-folder-name "csv")
(def data-folder-name (str "data/" base-folder-name))

(defn csv->map [f]
  (with-open [in-file (clojure.java.io/reader f)]
    (let [m (doall (csv/read-csv in-file))
          clean-m (filter (complement empty-row?) m)
          ks (map keyword (first clean-m))]
      (for [x (rest clean-m)]
        (into {} (zipmap ks x))))))

(defn map->remote-addresses [csv-map]
  (distinct (mapv #(:RemoteAddress %) csv-map)))

(defn file->filtered-addresses [file]
  (let [csv-map (csv->map (str data-folder-name "/" file))
        remote-addresses (map->remote-addresses csv-map)]
    (remove useless-ips remote-addresses)))

(defn get-data-files [folder-name]
  (let [data-folder (clojure.java.io/file folder-name)]
    (->> (file-seq data-folder) (mapv #(.getName %)) (filterv #(not= base-folder-name %)))))

(defn file->edges [file]
  (let [host-node (filename->keyword (first (clojure.string/split file #"\.csv")))
        connected-nodes (mapv (comp keyword addr->domain-name) (file->filtered-addresses file))
        edges (mapv (partial edgify host-node) connected-nodes)]
    edges))

(defn netstat-csvs->edges []
  (reduce #(apply conj %1 %2) [] (mapv file->edges (get-data-files data-folder-name))))