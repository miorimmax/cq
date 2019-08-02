(ns cq.cli
  (:gen-class)
  (:require [clojure.edn :as edn]
            [clojure.string :as string]
            [clojure.tools.cli :refer [parse-opts]]
            [zprint.core :as zprint]))

(def ^:private cli-options
  [["-d" "--default-reader-fn EXPR" "Default reader fn"
    :default      (fn [tag input] (format "#%s %s" tag input))
    :default-desc "Print tag and input values without further evaluation"
    :parse-fn     load-string]
   ["-f" "--from-file FILENAME" "Read expr from file"
    :default      nil
    :default-desc "Read expression from file rather than from a command line"]
   ["-c" "--colors" "Colorize the output"]
   ["-h" "--help"]])

(defn- show-usage [options]
  (string/join \newline ["Usage: cq [options] [expr]"
                         ""
                         "Options:"
                         options]))

(defn- show-errors [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn- validate-args [args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options) {:exit-message (show-usage summary) :exit-status 0}
      errors          {:exit-message (show-errors errors) :exit-status 1}
      :else           {:options (assoc options :expr (first arguments))})))

(defn- exit! [status message]
  (println message)
  (System/exit status))

(defn- eval! [{:keys [expr from-file default-reader-fn colors]}]
  (let [data      (edn/read {:default default-reader-fn} *in*)
        expr-fn   (cond
                    from-file (load-file from-file)
                    expr      (load-string expr)
                    :else     identity)
        pprint-fn (if colors zprint/czprint zprint/zprint)]
    (pprint-fn (expr-fn data))))

(defn -main [& args]
  (let [{:keys [options exit-status exit-message]} (validate-args args)]
    (if exit-message
      (exit! (or exit-status 1) exit-message)
      (eval! options))))
