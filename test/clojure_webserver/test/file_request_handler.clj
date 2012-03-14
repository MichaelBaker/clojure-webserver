(ns clojure-webserver.test.file-request-handler
  (:import java.io.File)
  (:use clojure.test)
  (:use clojure-webserver.file-request-handler))

(defn time-string [] (.toString (System/nanoTime)))
(def root (doto (File/createTempFile "temp" (time-string)) (.delete) (.mkdir)))
(def file1-name (.getName (File/createTempFile "temp" (time-string) root)))
(def file2-name (.getName (File/createTempFile "temp" (time-string) root)))

(deftest get-directory-contents
  (let [handler  (file-request-handler root)
        {:keys [status body headers]} (handler {:uri "/"})]
    (is (= 200 status))
    (is (= {}  headers))
    (is (.contains body file1-name))
    (is (.contains body file2-name))))