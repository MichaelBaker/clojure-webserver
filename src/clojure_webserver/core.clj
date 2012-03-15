(ns clojure-webserver.core
  (:import java.net.ServerSocket)
  (:import java.util.concurrent.Executors)
  (:use [clojure.java.io :only [file]])
  (:use clojure-webserver.http-handler)
  (:use clojure-webserver.request-handler)
  (:use clojure-webserver.option-parser)
  (:gen-class))

(defn accept-connection [server request-handler]
  (let [thread-pool (Executors/newFixedThreadPool 4)]
    (while true (.execute thread-pool
                          (http-handler (.accept server)
                                        request-handler)))))

(defn start-server [port request-handler]
  (println "[start] Server is awaiting connections on port " port)
  (println "[start] Press ^C to quit")
  (accept-connection (ServerSocket. port) request-handler))

(defn -main [& args]
  (let [[switches remaining usage] (parse-options args)
        handler (request-handler (file (switches :directory)))
        port    (switches :port)]
    (start-server port handler)))
