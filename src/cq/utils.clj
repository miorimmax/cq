(ns cq.utils
  (:require [clojure.java.io :as jio])
  (:import java.io.PushbackReader))

(defn assoc-if
  ([m k v]
   (if v (assoc m k v) m))
  ([m k v & kvs]
   (let [ret (assoc-if m k v)]
     (if kvs (recur ret (first kvs) (second kvs) (nnext kvs)) ret))))

(defn pushback-reader
  "Wraps a Reader from `clojure.java.io/reader` in a `java.io.PushbackReader`."
  [x & opts]
  (PushbackReader. (apply jio/reader x opts)))
