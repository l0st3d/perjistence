(ns perjistence.backends
  (:import [com.mchange.v2.c3p0 ComboPooledDataSource])
  (:require [perjistence.backends.mysql :as mysql]))

(def available-backends (ref {}))

(def known-backends (ref {}))

(defn add [name backend]
  (dosync
   (alter known-backends assoc name backend)))

(defn create-pooled-datasource [db-type username password hostname database-name &{:as options}]
  {:datasource (doto (new ComboPooledDataSource)
		 (.setDriverClass (-> available-backends db-type :driver-classname))
		 (.setUser username)
		 (.setPassword password)
		 (.setIdleConnectionTestPeriod (get options :idle-connection-test-period 3600))
		 (.setMinPoolSize (get options :min-pool-size 1))
		 (.setMaxPoolSize (get options :max-pool-size 25))
		 (.setMaxIdleTimeExcessConnections (get options :max-idle-time 1800))
		 (.setInitialPoolSize (get options :initial-pool-size 1))
		 (.setPreferredTestQuery (get options :test-query "SELECT 'testing database connection'"))
		 (.setJdbcUrl (str "jdbc:" (name db-type) "://" hostname "/" database-name)))
   :subprotocol "mysql"})

(defn init-backends []
  (dosync
   (alter available-backends assoc
	  :mysql mysql/backend-data)))

(defn get-fields-in-table [table-name]
  ['id 'name])