(ns cq.core
  (:require [clojure.edn :as edn]
            [cq.utils :as utils]
            [zprint.core :as zprint]))

(defn load-readers! [file-name]
  (when file-name (load-file file-name)))

(defn read-edn! [reader {:keys [default-reader-fn data-readers]}]
  (-> {:default default-reader-fn}
      (utils/assoc-if :readers (load-readers! data-readers))
      (edn/read reader)))

(defn filter-fn [{:keys [expr from-file]}]
  (cond
    from-file (load-file from-file)
    expr      (load-string expr)
    :else     identity))

(defn filter! [data options-map]
  ((filter-fn options-map) data))

(defn handle-input! [reader {:keys [colors] :as options-map}]
  (-> reader
      (read-edn! options-map)
      (filter! options-map)
      (zprint/zprint {:color? (boolean colors)})))

(defn handle-input-from-file! [file-name options-map]
  (with-open [reader (utils/pushback-reader file-name)]
    (handle-input! reader options-map)))

(defn handle-input-from-stdin! [options-map]
  (handle-input! *in* options-map))
