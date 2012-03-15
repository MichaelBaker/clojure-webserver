(ns clojure-webserver.http-handler
  (:import java.io.PrintWriter)
  (:use [clojure.java.io :only [reader]])
  (:use [clojure.string :only [trim]])
  (:use clojure-webserver.request-parser))

(def status-code-phrases
  {200 "OK"
   404 "Not Found"})

(def crlf "\r\n")

(defn status-line [status-code]
  (str "HTTP/1.1 "
       status-code
       " "
       (status-code-phrases status-code)
       crlf))

(defn http-handler [socket request-handler]
  (fn []
    (let [input  (.getInputStream socket)
          output (PrintWriter. (.getOutputStream socket))
          env    (parse-request (reader input))
          {:keys [status headers body]} (request-handler env)]
      (.. output
        (print (status-line status))
        (print crlf)
        (print body)
        (flush))
      (doseq [stream [input output socket]] (.close stream)))))
