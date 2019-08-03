(defproject cq "0.4.0-SNAPSHOT"
  :description "Command line tool to interact with EDN data/files"
  :main cq.cli
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/tools.cli "0.4.2"]
                 [zprint "0.4.16"]]
  :profiles {:uberjar {:aot :all}
             :dev     {:dependencies [[mockfn "0.4.0"]
                                      [nubank/matcher-combinators "1.0.1"]]
                       :plugins      [[lein-binplus "0.6.4"]
                                      [lein-cljfmt "0.6.4"]]}}
  :bin {:name          "cq"
        :bootclasspath true}
  :cljfmt {:indents {assoc-if  [[:inner 0]]
                     providing [[:block 0]]
                     verifying [[:block 0]]}})
