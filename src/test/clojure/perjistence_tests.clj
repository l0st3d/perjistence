(ns perjistence-tests
  (:use clojure.test)
  (:require
   [perjistence :as p]))

(deftest should-fail
  (is (= 1 0)))

(deftest should-initialise-sql-backend
  (p/create-sql-connection
   :test-connection-name
   :mysql
   "root"
   ""
   "127.0.0.1"
   "perjistence"
   :pooled-data-source)
  (is (= 1 0)))

(deftest should-define-domain
  (p/define-domain
    (sql-db :test-connection-name)
    (customer {:table "customers"}
	      (has-many order {:table "orders"}
			(has-many product)))
    (product {:table "products"}))
  (is (= 0 1)))

(run-tests)