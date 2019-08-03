(ns cq.core-test
  (:require [clojure.edn :as edn]
            [clojure.test :refer :all]
            [cq.core :as cq]
            [cq.utils :as utils]
            [cq.test-utils :refer [never once mock-closeable-reader]]
            [mockfn.clj-test :as mfn]
            [mockfn.matchers :refer [any] :rename {any anything}]
            [zprint.core :as zprint]))

(mfn/deftest load-readers-with-a-file-name-provided
  (is (= '..loaded-readers.. (cq/load-readers! '..file-name..)))
  (mfn/verifying (load-file '..file-name..) '..loaded-readers.. once))

(mfn/deftest load-readers-with-no-file-name-provided
  (is (nil? (cq/load-readers! nil)))
  (mfn/verifying (load-file anything) anything never))

(mfn/deftest read-edn-with-a-data-readers-file-name-provided
  (is (= '..edn.. (cq/read-edn! '..reader.. {:default-reader-fn '..default-reader-fn..
                                             :data-readers      '..data-readers-file..})))
  (mfn/verifying
    (cq/load-readers! '..data-readers-file..) '..loaded-readers.. once
    (edn/read {:default '..default-reader-fn.. :readers '..loaded-readers..} '..reader..) '..edn.. once))

(mfn/deftest read-edn-with-no-data-readers-file-name-provided
  (is (= '..edn.. (cq/read-edn! '..reader.. {:default-reader-fn '..default-reader-fn..})))
  (mfn/verifying
    (cq/load-readers! nil) nil once
    (edn/read {:default '..default-reader-fn..} '..reader..) '..edn.. once))

(mfn/deftest filter-fn-loads-filter-from-a-file
  (is (= '..filter.. (#'cq/filter-fn {:from-file '..file-name..})))
  (mfn/verifying
    (load-file '..file-name..) '..filter.. once
    (load-string anything) anything never))

(mfn/deftest filter-fn-loads-filter-from-a-string
  (is (= '..filter.. (#'cq/filter-fn {:expr '..expr..})))
  (mfn/verifying
    (load-file anything) anything never
    (load-string '..expr..) '..filter.. once))

(mfn/deftest filter-fn-fallback-to-identity
  (is (= identity (#'cq/filter-fn nil)))
  (mfn/verifying
    (load-file anything) anything never
    (load-string anything) anything never))

(mfn/deftest filter-applies-the-filter-fn-to-data
  (is (= '..filtered.. (cq/filter! '..data.. '..options..)))
  (mfn/verifying
    (cq/filter-fn '..options..) (constantly '..filtered..) once))

(mfn/deftest handle-input-parses-the-edn-data-filters-and-pretty-prints
  (is (= '..result.. (cq/handle-input! '..reader.. '..options..)))
  (mfn/verifying
    (cq/read-edn! '..reader.. '..options..) '..data.. once
    (cq/filter! '..data.. '..options..) '..filtered.. once
    (zprint/zprint '..filtered.. {:color? false}) '..result.. once))

(mfn/deftest handle-input-from-file-reads-the-edn-data-from-a-file
  (is (= '..result.. (cq/handle-input-from-file! '..file-name.. '..options..)))
  (mfn/verifying
    (utils/pushback-reader '..file-name..) mock-closeable-reader once
    (cq/handle-input! mock-closeable-reader '..options..) '..result.. once))

(mfn/deftest handle-input-from-stdin-reads-the-edn-data-from-stdin
  (is (= '..result.. (cq/handle-input-from-stdin! '..options..)))
  (mfn/verifying (cq/handle-input! *in* '..options..) '..result.. once))
