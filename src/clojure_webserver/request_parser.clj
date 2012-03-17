(ns clojure-webserver.request-parser
  (:import java.io.BufferedReader)
  (:use [clojure.string :only [split-lines split]]))

(defn -parse-request-line [line]
  (let [[method uri version] (split line #"\s")]
    {:method method :uri uri :version version :remaining rest}))

(defn parse-request
  "Takes a reader and returns a map of request parameters."
  [reader]
  (let [[first & rest] (line-seq (BufferedReader. reader))]
    (-parse-request-line first)))
