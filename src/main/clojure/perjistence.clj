(ns perjistence
  (:require [perjistence.backends :as backends]))

(defn create-sql-connection [name db-type username password host db-name & options]
  (backends/add name (backends/create-pooled-datasource db-type username password host db-name)))

(defmacro define-domain [& definitions]
  )