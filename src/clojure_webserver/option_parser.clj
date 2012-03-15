(ns clojure-webserver.option-parser
  (:use clojure.tools.cli))

(defn -parse-args [args]
  (cli args
    ["-p"
     "--port"
     "Start the server on this port."
     :parse-fn #(Integer. %)
     :default 5000]
    ["-d"
     "--directory"
     "The directory from which files will be served."
     :default "public"]))

(defn parse-options [args]
  (let [[{:keys [directory port]}] (-parse-args args)]
    [directory port]))
