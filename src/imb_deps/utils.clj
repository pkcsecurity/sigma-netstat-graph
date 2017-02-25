(ns imb-deps.utils
  (:require [clojure.java.io :as io]
            [clj-dns.core :as dns]))

(def dns-cache-map (into {} (mapv #(let [[k v] (clojure.string/split % #",")] {k v}) (clojure.string/split (slurp "dns-cache") #"\n"))))

(defn expand-edges [x [y1 y2]]
  (apply conj x (mapv #(conj [y1] %) y2)))

; map is structured {:node1 [:edge2 :edge3 ...], :node2 [:edge3 :edge4 ...]}
(defn generate-edges-from-map [edges-map]
  (reduce expand-edges [] edges-map))

; takes an array of edges [[node1 node2] [node3 node2] ... [...]] and returns an array of nodes
(defn edges->nodes [edges-vec]
  (distinct (flatten edges-vec)))

(def useless-ips #{"0.0.0.0", "*", "RemoteAddress"})

(defn empty-row? [row]
  (every? clojure.string/blank? row))

; Process files stuff
(defn addr->domain-name [addr]
  (if-let [result (dns-cache-map addr)]
    result
    (let [domain-name (if-let [name (dns/hostname addr)] name addr)]
      (spit "dns-cache" (str addr "," domain-name "\n") :append true)
      domain-name)))

(defn filename->keyword [fileName]
  (-> fileName .toLowerCase keyword))

(defn edgify [host-node connected-node]
  [host-node connected-node])