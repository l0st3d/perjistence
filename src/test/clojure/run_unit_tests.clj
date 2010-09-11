(ns run-unit-tests
  (:use test-runner))

(run-tests-and-exit "unit" "src/test/clojure")