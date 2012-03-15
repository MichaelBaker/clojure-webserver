(ns clojure-webserver.test.request-handler
  (:import java.io.File)
  (:use clojure.test)
  (:use clojure-webserver.request-handler))

(defn time-string [] (.toString (System/nanoTime)))
(def root (doto (File/createTempFile "temp" (time-string)) (.delete) (.mkdir)))
(def file1-name (.getName (File/createTempFile "temp" (time-string) root)))
(def file2-name (.getName (File/createTempFile "temp" (time-string) root)))

(testing "request-handler"
  (def handler (request-handler root))
  (deftest get-directory-contents
    (let [{:keys [status body headers]} (handler {:uri "/"})]
      (is (= 200 status))
      (is (= {}  headers))
      (is (.contains body file1-name))
      (is (.contains body file2-name))))

  (deftest file-does-not-exist
    (let [{:keys [status body headers]} (handler {:uri "/file3"})]
      (is (= 404 status)))))
