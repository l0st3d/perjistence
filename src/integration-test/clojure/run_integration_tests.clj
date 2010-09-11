(ns run-unit-tests
  (:use rivelin.test-runner))

(run-tests-and-exit "integration" "src/integration-test/clojure")