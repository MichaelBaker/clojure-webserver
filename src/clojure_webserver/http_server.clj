(ns clojure-webserver.http-server
  (:import java.net.ServerSocket)
  (:import java.util.concurrent.Executors)
  (:use clojure-webserver.http-handler))

(defn -accept-connection [server request-handler]
  (let [thread-pool (Executors/newFixedThreadPool 10)]
    (while true (.execute thread-pool
                          (http-handler (.accept server)
                                        request-handler)))))

(defn -create-server [port]
  (doto (ServerSocket. port)
        (.setReuseAddress true)))

(defn start-server
  ([port request-handler]
    (start-server port request-handler (fn [server])))
  ([port request-handler start-callback]
    (let [server (-create-server port)]
      (start-callback server)
      (-accept-connection server request-handler))))
