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
  (p/define-domain
    (sql-db :test-connection-name)
    (Customer {:table "customers"}
	      (has-many Order {:table "orders"}
			(has-many Product)))
    (Product {:table "products"}))
  (is (class? Customer))
  (is (class? Order))
  (is (class? Product)))

(println "\n\n\n ==============================\n")
(run-tests)