(ns cq.cli
  (:gen-class)
  (:require [clojure.edn :as edn]
            [clojure.java.io :as jio]
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
   ["-r" "--data-readers FILENAME" "Data readers file"
    :default      nil
    :default-desc "Read data readers from file"]
   ["-c" "--colors" "Colorize the output"]
   ["-h" "--help"]])

(defn- show-usage [options]
  (string/join \newline ["Usage: cq [options] [expr] [files]"
                         ""
                         "Options:"
                         options]))

(defn- show-errors [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn- assoc-if
  ([m k v]
   (if v (assoc m k v) m))
  ([m k v & kvs]
   (let [ret (assoc-if m k v)]
     (if kvs (recur ret (first kvs) (second kvs) (nnext kvs)) ret))))

(defn- validate-args [args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options) {:exit-message (show-usage summary) :exit-status 0}
      errors          {:exit-message (show-errors errors) :exit-status 1}
      :else           {:options (assoc-if options
                                  :expr  (when-not (:from-file options) (first arguments))
                                  :files (if-not (:from-file options) (rest arguments) arguments))})))

(defn- exit! [status message]
  (println message)
  (System/exit status))

(defn- load-readers! [data-readers]
  (when data-readers (load-file data-readers)))

(defn- read-input! [default-reader-fn data-readers input]
  (-> {:default default-reader-fn}
      (assoc-if :readers (load-readers! data-readers))
      (edn/read input)))

(defn- filter-input! [from-file expr input]
  (let [filter-fn (cond
                    from-file (load-file from-file)
                    expr      (load-string expr)
                    :else     identity)]
    (filter-fn input)))

(defn- handle-input! [{:keys [default-reader-fn data-readers from-file expr]} pprint-fn input]
  (->> input
       (read-input! default-reader-fn data-readers)
       (filter-input! from-file expr)
       pprint-fn))

(defn- handle-input-from-file! [options pprint-fn file]
  (try
    (with-open [reader (jio/reader file)]
      (handle-input! options pprint-fn (java.io.PushbackReader. reader)))
    (catch java.io.IOException e
      (exit! 1 (format "Could not open '%s': %s" file (.getMessage e))))))

(defn- handle-input-from-stdin! [options pprint-fn]
  (handle-input! options pprint-fn *in*))

(defn- handle-inputs! [{:keys [colors files] :as options}]
  (let [pprint-fn (if colors zprint/czprint zprint/zprint)]
    (if (seq files)
      (run! #(handle-input-from-file! options pprint-fn %) files)
      (handle-input-from-stdin! options pprint-fn))))

(defn -main [& args]
  (let [{:keys [options exit-status exit-message]} (validate-args args)]
    (if exit-message
      (exit! (or exit-status 1) exit-message)
      (handle-inputs! options))))
