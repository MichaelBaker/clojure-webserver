(ns clojure-webserver.core
  (:use [clojure.java.io :only [file]])
  (:use clojure-webserver.option-parser)
  (:use clojure-webserver.http-server)
  (:use clojure-webserver.request-handler)
  (:gen-class))

(defn -main [& args]
  (let [[directory port] (parse-options args)
        static-file-root (file directory)
        handler          (request-handler static-file-root)]
    (start-server port handler (fn [server]
      (println "[start] Server is awaiting connections on port " port)
      (println "[start] Press ^C to quit")))))
