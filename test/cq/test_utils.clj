(ns cq.test-utils
  (:require [mockfn.matchers :as mfn.matchers]))

(def once (mfn.matchers/exactly 1))
(def never (mfn.matchers/exactly 0))

(def mock-closeable-reader
  "A mock for a `java.io.Reader` to be used in tests where call to `with-open` is made"
  (proxy [java.io.Reader] [(java.io.StringReader. "{:mock true}")]
    (close [])))
