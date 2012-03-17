(ns clojure-webserver.request-handler
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
    (join "\n" (map #(-relative-path file %) (file-seq file)))
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

(defn -get-handler [env, filename-map]
  (if (filename-map (:uri env))
      {:status 200 :headers {} :body (filename-map (env :uri))}
      {:status 404 :headers {} :body ""}))

(defn -post-handler []
  {:status 200 :headers {} :body ""})

(defn -put-handler []
  {:status 200 :headers {} :body ""})

(defn request-handler
  "Takes a directory and returns a function that acts as a request handler for serving static files from that directory.
  The returned function takes a map of request parameters and returns a map containing the status, headers, and body of the response."
  [root]
  (let [filename-map (-create-filename-map root)]
    (fn [env] (cond
      (= "GET"  (:method env)) (-get-handler env filename-map)
      (= "POST" (:method env)) (-post-handler)
      (= "PUT"  (:method env)) (-put-handler)))))
