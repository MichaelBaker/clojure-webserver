(ns clojure-webserver.test.request-parser
  (:use clojure.test)
  (:use clojure-webserver.request-parser))

(deftest get-root
  (def request "GET / HTTP/1.1\r\n")
  (let [{:keys [method uri version]} (parse-request request)]
    (is (= "GET"      method))
    (is (= "/"        uri))
    (is (= "HTTP/1.1" version))))
