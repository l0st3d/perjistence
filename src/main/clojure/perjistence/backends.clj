(ns perjistence.backends
  (:import [com.mchange.v2.c3p0 ComboPooledDataSource])
  (:require [perjistence.backends.mysql :as mysql]))

(def available-backends (ref {}))

(def known-backends (ref {}))

(defn add [name backend]
  (alter known-backends assoc name backend))

(defn create-pooled-datasource [db-type username password hostname database-name options]
  (doto (new ComboPooledDataSource)
    (.setDriverClass (-> available-backends db-type :driver-classname))
    (.setUser username)
    (.setPassword password)
    (.setIdleConnectionTestPeriod (get options :idle-connection-test-period 3600))
    (.setMinPoolSize (get options :min-pool-size 1))
    (.setMaxPoolSize (get options :max-pool-size 25))
    (.setMaxIdleTimeExcessConnections (get :max-idle-time 1800))
    (.setInitialPoolSize (get :initial-pool-size 1))
    (.setPreferredTestQuery (get :test-query "select 'testing database connection'"))
    (.setJdbcUrl (str "jdbc:mysql://" hostname "/" database-name))))