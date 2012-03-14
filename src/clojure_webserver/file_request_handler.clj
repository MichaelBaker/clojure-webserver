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

(defn -create-filename-map [root]
  ((fn [files filename-map]
     (if (seq files)
       (recur (rest files)
              (assoc filename-map
                     (-relative-path root (first files))
                     (-reponse (first files))))
       filename-map)) [root] {}))

(defn file-request-handler [root]
  (def filename-map (-create-filename-map root))
  (fn [env] {:status 200 :headers {} :body (filename-map (env :uri))}))
