(ns clojure-webserver.request-parser
  (:use [clojure.string :only [split-lines split]]))

(defn -get-request-line [{[line & rest] :remaining}]
  (let [[method uri version] (split line #"\s")]
    {:method method :uri uri :version version :remaining rest}))

(defn -get-headers [request]
  (dissoc request :remaining))

(defn parse-request [string]
  (-> {:remaining (line-seq string)}
      (-get-request-line)
      (-get-headers)))
