(ns clojure-webserver.http-handler
  (:import java.io.PrintWriter)
  (:use [clojure.java.io :only [reader]])
  (:use [clojure.string :only [trim]])
  (:use clojure-webserver.request-parser))

(defn http-handler [socket request-handler]
  (fn []
    (let [input  (.getInputStream socket)
          output (PrintWriter. (.getOutputStream socket))
          env    (parse-request (reader input))]
      (.print output (:body (request-handler env)))
      (.flush output)
      (.close input)
      (.close output)
      (.close socket))))
