(ns cq.cli
  (:gen-class)
  (:require [clojure.tools.cli :as tools.cli]
            [cq.core :as cq]
            [cq.utils :as utils]))

(def cli-options
  [["-d" "--default-reader-fn EXPR" "Default reader fn"
    :default      (fn [tag input] (format "#%s %s" tag input))
    :default-desc "Print tag and input values without further evaluation"
    :parse-fn     load-string]
   ["-f" "--from-file FILENAME" "Read expression from file rather than from a command line"]
   ["-r" "--data-readers FILENAME" "Read data readers from file"]
   ["-c" "--colors" "Colorize the output"]
   ["-h" "--help"]])

(defn show-usage [options]
  (str "Usage: cq [options] [expr] [files]"
       \newline
       \newline
       "Options:"
       \newline
       options))

(defn show-errors [errors]
  (apply (partial str "The following errors occurred while parsing your command:" \newline \newline)
         (interpose \newline errors)))

(defn parse-args [args]
  (let [{:keys [options arguments errors summary]} (tools.cli/parse-opts args cli-options)]
    (cond
      (:help options) {:exit-message (show-usage summary) :exit-status 0}
      errors          {:exit-message (show-errors errors) :exit-status 1}
      :else           {:options (utils/assoc-if options
                                  :expr  (when-not (:from-file options) (first arguments))
                                  :files (seq (if-not (:from-file options) (rest arguments) arguments)))})))

(defn exit! [status message]
  (println message)
  (System/exit status))

(defn handle-input! [{:keys [files] :as options}]
  (if (seq files)
    (run! #(cq/handle-input-from-file! % options) files)
    (cq/handle-input-from-stdin! options)))

(defn -main [& args]
  (let [{:keys [options exit-status exit-message]} (parse-args args)]
    (if exit-message
      (exit! (or exit-status 1) exit-message)
      (handle-input! options))))
