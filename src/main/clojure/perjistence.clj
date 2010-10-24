(ns perjistence
  (:require
   [perjistence.backends :as backends]
   [clojure.contrib.sql :as sql]
   [perjistence.domain :as domain]))

(defn create-sql-connection [name db-type username password host db-name & options]
  (backends/add name (backends/create-pooled-datasource db-type username password host db-name)))

(defmacro define-domain [& definitions]
  )

(defn execute-query [sql connection]
  (sql/with-connection connection
    (sql/with-query-results rows [sql]
      (doall rows))))

(defmacro defentity [entity table-name]
  `(defrecord ~entity ~(backends/get-fields-in-table table-name)))

(defn one-to-many [parent child]
  )