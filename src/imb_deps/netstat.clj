(ns imb-deps.netstat
  (:require [clojure.data.csv :as csv]
            [imb-deps.utils :refer [useless-ips empty-row? addr->domain-name filename->keyword edgify]]))

(def base-folder-name "netstats")
(def data-folder-name (str "data/" base-folder-name))

(defn raw-netstat->well-formatted [raw]
  (let [columns (remove nil? (mapv #(let [raw-text (clojure.string/trim (first %))
                            columns (clojure.string/split raw-text #"\s\s+")]
                        (when (= (count columns) 4)
                          columns)) raw))
        headers (first columns)]
    (if (not= headers ["Proto" "Local Address" "Foreign Address" "State"])
      (cons ["Proto" "Local Address" "Foreign Address" "State"] columns)
      columns)))

(def translations
  {"Proto"            :proto
   "Local Address"    :local-address
   "Foreign Address"  :foreign-address
   "State"            :state})

(defn csv->map [f]
  (with-open [in-file (clojure.java.io/reader f)]
    (let [raw (doall (csv/read-csv in-file))
          m (raw-netstat->well-formatted raw)
          ks (map #(get translations %) (first m))]
      (for [x (rest m)]
        (into {} (zipmap ks x))))))

(defn just-ip [row]
  (let [ip (:foreign-address row)]
    (when-not (clojure.string/index-of ip "[")
      (first (clojure.string/split ip #"\:")))))

(defn map->remote-addresses [csv-map]
  (distinct (remove nil? (mapv just-ip csv-map))))

(defn file->filtered-addresses [file]
  (let [csv-map (csv->map (str data-folder-name "/" file))
        remote-addresses (map->remote-addresses csv-map)]
    (remove useless-ips remote-addresses)))

(defn get-data-files [folder-name]
  (let [data-folder (clojure.java.io/file folder-name)]
    (->> (file-seq data-folder) (mapv #(.getName %)) (filterv #(not= base-folder-name %)))))

(defn file->edges [file]
  (let [host-node (filename->keyword file)
        connected-nodes (mapv (comp keyword addr->domain-name) (file->filtered-addresses file))
        edges (mapv (partial edgify host-node) connected-nodes)]
    edges))

(defn netstat-raw->edges []
  (reduce #(apply conj %1 %2) [] (mapv file->edges (get-data-files data-folder-name))))