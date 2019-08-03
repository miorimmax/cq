(ns cq.utils-test
  (:require [clojure.test :refer :all]
            [cq.utils :as utils]))

(deftest assoc-if-tests
  (are [expected actual] (= expected actual)
    {:a 1 :b 2}      (utils/assoc-if {:a 1} :b 2)
    {:a 1}           (utils/assoc-if {:a 1} :b nil)
    {:a 1}           (utils/assoc-if {:a 1} :b false)
    {:a 1 :b 2 :c 3} (utils/assoc-if {:a 1} :b 2 :c 3)
    {:a 1 :b 2}      (utils/assoc-if {:a 1} :b 2 :c nil)
    {:a 1 :c 3}      (utils/assoc-if {:a 1} :b nil :c 3)
    {:a 1 :b 2 :c 3} (utils/assoc-if {:a 1} :b 2 :c 3 :d)
    {:b 2}           (utils/assoc-if nil    :b 2)
    nil              (utils/assoc-if nil    :b nil)
    {:b 2 :c 3}      (utils/assoc-if nil    :b 2 :c 3)
    {:b 2}           (utils/assoc-if nil    :b 2 :c nil)
    {:c 3}           (utils/assoc-if nil    :b nil :c 3)
    {:a 2}           (utils/assoc-if {:a 1} :a 2)))
