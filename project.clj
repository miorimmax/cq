(defproject cq "0.4.0"
  :description "Command line tool to interact with EDN data/files"
  :main cq.cli
  :exclusions [org.clojure/clojure]
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/tools.cli "0.4.2"]
                 [zprint "0.4.16"]]
  :mirrors [["central" {:url "https://jcenter.bintray.com/"}]]
  :profiles {:uberjar {:aot :all}
             :kaocha  {:dependencies [[lambdaisland/kaocha "0.0-529"]]}
             :dev     {:dependencies [[mockfn "0.4.0" :exclusions [midje]]
                                      [nubank/matcher-combinators "1.0.1" :exclusions [midje]]]
                       :plugins      [[lein-binplus "0.6.5"]
                                      [lein-cljfmt "0.6.4"]]
                       :mirrors      [["clojure-releases" {:url "https://oss.sonatype.org/content/groups/public/"}]]}}
  :bin {:name          "cq"
        :bootclasspath true}
  :cljfmt {:indents {assoc-if  [[:inner 0]]
                     providing [[:block 0]]
                     verifying [[:block 0]]}}
  :aliases {"kaocha" ["with-profile" "+kaocha" "run" "-m" "kaocha.runner"]})
