(ns cq.cli
  (:gen-class)
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.pprint :refer [pprint]]
            [clojure.string :as string]
            [clojure.edn :as edn]))

(defn default-reader-fn [tag input]
  (format "#%s %s" tag input))

(def ^:private cli-options
  [["-h" "--help"]])

(defn- show-usage [options]
  (->> ["Usage: cq [options] [expr]"
        ""
        "Options:"
        options]
       (string/join \newline)))

(defn- show-errors [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn- validate-args [args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options)
      {:exit-message (show-usage summary) :exit-status 0}

      errors
      {:exit-message (show-errors errors)}

      :else
      {:expr (first arguments) :options options})))

(defn- exit! [status message]
  (println message)
  (System/exit status))

(defn- eval! [expr]
  (let [data (edn/read {:default default-reader-fn} *in*)
        f    (some->> expr str read-string eval)]
    (if f
      (pprint (f data))
      (pprint data))))

(defn -main [& args]
  (let [{:keys [expr options exit-status exit-message]} (validate-args args)]
    (if exit-message
      (exit! (or exit-status 0) exit-message)
      (eval! expr))))
