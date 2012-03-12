(ns clojure-webserver.core)

(use 'clojure.tools.cli)

(defn -main [& args]
  (println (cli args
       ["-p" "--port" "Start the server on this port." :parse-fn #(Integer. %)]
       ["-d" "--directory" "The directory from which files will be served."])))
