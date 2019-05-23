(defproject cq "0.3.0"
  :description "Command line tool to interact with EDN data/files"
  :main cq.cli
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/tools.cli "0.4.1"]
                 [zprint "0.4.15"]]
  :profiles {:uberjar {:aot :all}
             :dev {:plugins [[lein-binplus "0.6.4"]]}}
  :bin {:bootclasspath true})
