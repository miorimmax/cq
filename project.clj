(defproject cq "0.4.0-SNAPSHOT"
  :description "Command line tool to interact with EDN data/files"
  :main cq.cli
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/tools.cli "0.4.2"]
                 [zprint "0.4.16"]]
  :profiles {:uberjar {:aot :all}
             :dev     {:plugins [[lein-binplus "0.6.4"]
                                 [lein-cljfmt "0.6.4"]]}}
  :bin {:bootclasspath true}
  :cljfmt {:indents {assoc-if [[:inner 0]]}})
