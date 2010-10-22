(ns perjistence.domain)

(def entities (ref {}))

(def sql-connection-name)

(defn add-entity [entity table-name]
  (dosync
   (alter entities assoc entity {:symbol entity
				 :table-name table-name})))


(defmulti parse-element #(keyword (first %1)))

(defmethod parse-element :sql-db [definition-element]
	   (set! sql-connection-name (second definition-element)))

(defmethod parse-element :default [definition-element]
	   (let [entity (first definition-element)
		 {table-name :table} (second definition-element)
		 remainder (-> definition-element rest rest)]
	     `(do
		(add-entity ~entity table-name)
		(defrecord ~entity [id name]))))

(defn parse [definitions]
  (println "\n\n ")
  (let [d (map parse-element definitions)]
    (prn d)
    d))
