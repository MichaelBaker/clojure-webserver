(ns clojure-webserver.core
  (:import java.net.ServerSocket)
  (:use [clojure.java.io :only [file]])
  (:use clojure.tools.cli)
  (:use clojure-webserver.http-handler)
  (:use clojure-webserver.file-request-handler)
  (:gen-class))

(def handler)

(defn accept-connection [server]
  (let [socket (.accept server)]
    (println (str "[connect] " socket))
    (.. (Thread. (http-handler socket handler))
        (start))
    (recur server)))

(defn start-server [port]
  (println "[start] Server is awaiting connections on port " port)
  (println "[start] Press ^C to quit")
  (accept-connection (ServerSocket. port)))

(defn -main [& args]
  (let [[switches remaining usage] (cli args
       ["-p" "--port" "Start the server on this port." :parse-fn #(Integer. %) :default 5000]
       ["-d" "--directory" "The directory from which files will be served." :default "public"])]
    (def handler (file-request-handler (file (switches :directory))))
    (start-server (switches :port))))
