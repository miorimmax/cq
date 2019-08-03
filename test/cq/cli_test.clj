(ns cq.cli-test
  (:require [clojure.test :refer :all]
            [clojure.tools.cli :as tools.cli]
            [cq.cli :as cli]
            [cq.core :as cq]
            [cq.test-utils :refer [once never]]
            [matcher-combinators.clj-test] ;; for match?
            [matcher-combinators.matchers :as m]
            [mockfn.clj-test :as mfn]
            [mockfn.matchers :refer [any] :rename {any anything}]))

(deftest show-usage-returns-the-usage-string
  (is (= (str "Usage: cq [options] [expr] [files]" \newline \newline "Options:" \newline "{{options}}")
         (cli/show-usage "{{options}}"))))

(deftest show-error-returns-an-error-string
  (is (= (str "The following errors occurred while parsing your command:" \newline \newline "a" \newline "b")
         (cli/show-errors ["a" "b"]))))

(mfn/deftest parse-args-shows-usage-if-help-is-present
  (is (= {:exit-message '..exit-message.. :exit-status 0} (cli/parse-args '..args..)))
  (mfn/verifying
    (tools.cli/parse-opts '..args.. cli/cli-options) {:options {:help true} :summary '..summary..} once
    (cli/show-usage '..summary..) '..exit-message.. once))

(mfn/deftest parse-args-shows-errors-if-any
  (is (= {:exit-message '..exit-message.. :exit-status 1} (cli/parse-args '..args..)))
  (mfn/verifying
    (tools.cli/parse-opts '..args.. cli/cli-options) {:errors '..errors..} once
    (cli/show-errors '..errors..) '..exit-message.. once))

(deftest parse-args-tests
  (are [matcher args] (match? matcher (cli/parse-args args))
    (m/embeds {:options {:default-reader-fn str}})                                    ["-d" "str"]
    (m/embeds {:options {:data-readers "data_readers.clj"}})                          ["-r" "data_readers.clj"]
    (m/embeds {:options {:colors true}})                                              ["-c"]
    (m/embeds {:options {:from-file m/absent :expr "expr" :files m/absent}})          ["expr"]
    (m/embeds {:options {:from-file m/absent :expr "expr" :files '("file.edn")}})     ["expr" "file.edn"]
    (m/embeds {:options {:from-file "file.clj" :expr m/absent :files m/absent}})      ["-f" "file.clj"]
    (m/embeds {:options {:from-file "file.clj" :expr m/absent :files '("file.edn")}}) ["-f" "file.clj" "file.edn"]))

(mfn/deftest handle-input-from-file
  (is (any? (cli/handle-input! {:files '("file.edn" "another-file.edn")})))
  (mfn/verifying
    (cq/handle-input-from-file! "file.edn" {:files '("file.edn" "another-file.edn")}) anything once
    (cq/handle-input-from-file! "another-file.edn" {:files '("file.edn" "another-file.edn")}) anything once))

(mfn/deftest handle-input-from-stdin
  (is (any? (cli/handle-input! '..options..)))
  (mfn/verifying (cq/handle-input-from-stdin! '..options..) anything once))

(mfn/deftest main-with-errors
  (is (any? (cli/-main '..args..)))
  (mfn/verifying
    (cli/parse-args ['..args..]) {:exit-message "error" :exit-status 1} once
    (cli/exit! 1 "error") anything once
    (cli/handle-input! anything) anything never))

(mfn/deftest main-with-no-errors
  (is (any? (cli/-main '..args..)))
  (mfn/verifying
    (cli/parse-args ['..args..]) {:options {:a-option true}} once
    (cli/exit! anything anything) anything never
    (cli/handle-input! {:a-option true}) anything once))
