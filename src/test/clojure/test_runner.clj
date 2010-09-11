(ns test-runner
  (:require [clojure.test.junit :as junit]
	    [clojure.test :as test]
	    [clojure.contrib.duck-streams :as duck]
	    [clojure.contrib.find-namespaces :as find-ns]))

(def results (atom []))

(defn report [m]
  (swap! results conj m)
  (junit/junit-report m))

(defmacro with-junit-output
  "taken and adapted from clojure.test.junit"
  [& body]
  `(test/with-test-out
     (binding [test/report report
	       junit/*var-context* (list)
	       junit/*depth* 0]
       (println "<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
       (let [result# ~@body]
	 result#))))

(defn run-tests-and-exit [test-type-name src-dir-name]
  (let [test-namespaces (find-ns/find-namespaces-in-dir (java.io.File. src-dir-name))]
    (apply require test-namespaces) 
    (println "Running" test-type-name "tests for namespaces:" test-namespaces)
    (.mkdir (java.io.File. "./target/test-reports/"))
    (when-not *compile-files*
      (doseq [namespace test-namespaces]
	(let [result (binding [test/*test-out* (duck/writer (str "target/test-reports/TEST-" test-type-name "-" namespace ".xml"))]
		       (with-junit-output
			 (test/run-tests namespace)))]
	  (if (< 0 (+ (result :fail) (result :error)))
	    (println "[FAILURE] in " namespace " : " result))))
      (Thread/sleep 500)
      (shutdown-agents)
      (if (some #(< 0 (+ (get % :fail 0)
			 (get % :error 0)))
		@results)
	(do
	  (dorun
	   (map test/report @results))
	  (System/exit 127))
	(System/exit 0)))))
