(ns cq
  (:require [clojure.pprint :refer [pprint]]
            [clojure.edn :as edn]))

(defn default-reader-fn [tag input]
  (format "#%s %s" tag input))

(defn -main [& args]
  (let [data (edn/read {:default default-reader-fn} *in*)
        f    (some->> args first str read-string eval)]
    (if f
      (pprint (f data))
      (pprint data))))
