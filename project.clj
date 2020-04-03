(defproject cq "0.4.0"
  :description "Command line tool to interact with EDN data/files"
  :main cq.cli
  :exclusions [org.clojure/clojure]
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/tools.cli "0.4.2"]
                 [zprint "0.5.4"]]
  :mirrors [["central" {:name "jcenter" :url "https://jcenter.bintray.com/"}]]
  :profiles {:uberjar {:aot :all}
             :kaocha  {:dependencies [[lambdaisland/kaocha "1.0-612"]]}
             :dev     {:dependencies [[nubank/mockfn "0.6.0"]
                                      [nubank/matcher-combinators "1.5.1" :exclusions [midje]]
                                      [clj-kondo "2020.03.20"]]
                       :plugins      [[lein-binplus "0.6.5"]
                                      [lein-cljfmt "0.6.4"]]}}
  :bin {:name "cq"}
  :cljfmt {:indents {assoc-if  [[:inner 0]]
                     providing [[:block 0]]
                     verifying [[:block 0]]}}
  :aliases {"kaocha"    ["with-profile" "+kaocha" "run" "-m" "kaocha.runner"]
            "clj-kondo" ["run" "-m" "clj-kondo.main" "--lint" "src" "--config" ".clj-kondo/config.edn" "--cache"]})
