(ns clojure-webserver.core
  (:use clojure.tools.cli)
  (:import java.net.ServerSocket)
  (:gen-class))

(defn worker [socket]
  (println (str "Got a socket:" socket))
  (.close socket))

(defn accept-connection [server]
  (Thread. (worker (.accept server)))
  (recur server))

(defn start-server [port]
  (accept-connection (ServerSocket. port)))

(defn -main [& args]
  (let [[switches remaining usage] (cli args
       ["-p" "--port" "Start the server on this port." :parse-fn #(Integer. %) :default 5000]
       ["-d" "--directory" "The directory from which files will be served." :default "public"])]
    (start-server (switches :port))))
