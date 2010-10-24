(ns perjistence-tests
  (:use clojure.test)
  (:require
   [perjistence :as p]))

(deftest should-fail
  (is (= 1 1)))

(deftest should-initialise-sql-backend
  (let [conn (p/create-sql-connection
	      :test-connection-name
	      :mysql
	      "root"
	      ""
	      "127.0.0.1"
	      "perjistence"
	      :pooled-data-source)]
    (is (= [{:n 1}] (p/execute-query "SELECT 1 AS n" (:test-connection-name conn))))))

(deftest should-define-domain
  (p/defentity Customer "customers")
  (p/defentity Product "products")
  (p/defentity Order "orders")
  (p/one-to-many Customer Order)
  (p/one-to-many Order Product)

  (is (class? Customer))
  (is (class? Order))
  (is (class? Product))

  )



(println "\n\n\n ==============================\n")
(run-tests)