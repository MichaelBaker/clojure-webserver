(ns clojure-webserver.file-request-handler
  (:use [clojure.string :only [replace-first join]]))

(defn -relative-path [root file]
  (let [root-path (.getAbsolutePath root)
        file-path (.getAbsolutePath file)
        relative-path (replace-first file-path root-path "")]
    (if (re-matches #"^/.*" relative-path)
      relative-path
      (str "/" relative-path))))

(defn -reponse [file]
  (if (.isDirectory file)
    (join "\n" (map (partial -relative-path file) (file-seq file)))
    (slurp file)))

(defn -remaining-files [files]
  (let [file (first files)]
    (if (.isDirectory file)
      (into (rest files) (remove #(= file %) (file-seq file)))
      (rest files))))

(defn -create-filename-map [root]
  ((fn [files filename-map]
     (if (seq files)
       (recur (-remaining-files files)
              (assoc filename-map
                     (-relative-path root (first files))
                     (-reponse (first files))))
       filename-map)) [root] {}))

(defn file-request-handler [root]
  (def filename-map (-create-filename-map root))
  (fn [env]
    (if (filename-map (:uri env))
      {:status 200 :headers {} :body (filename-map (env :uri))}
      {:status 404 :headers {} :body ""})))
