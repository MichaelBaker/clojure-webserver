(ns clojure-webserver.core
  (:use clojure.tools.cli)
  (:import java.io.File)
  (:gen-class))

(defn resource-path [relative-path]
  (.. (File. relative-path) (getAbsolutePath)))

(defn -main [& args]
  (let [[switches remaining usage] (cli args
       ["-p" "--port" "Start the server on this port." :parse-fn #(Integer. %) :default 5000]
       ["-d" "--directory" "The directory from which files will be served." :default "public"])]
    ))
