(ns perjistence.backends.mysql)

(dosync
 (alter perjistence.backends/available-backends
	assoc-in [:mysql :driver-classname] "com.mysql.jdbc.Driver"))