(defproject imb-deps "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [lacij "LATEST"]
                 [cheshire "LATEST"]
                 [org.clojure/data.csv "LATEST"]
                 [net.apribase/clj-dns "0.1.0"]]
  :main ^:skip-aot imb-deps.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
