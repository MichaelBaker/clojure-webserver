(ns clojure-webserver.test.http_parser
  (:use clojure.test)
  (:use clojure-webserver.http_parser))

(testing "Request methods"
  (deftest acceptable-methods
    (are [method] (= [method] (:matches (request-method method)))
         "OPTIONS"
         "GET"
         "HEAD"
         "POST"
         "PUT"
         "DELETE"
         "TRACE"
         "CONNECT")))
  (deftest bogus-methods
    (are [method] (= [] (:matches (request-method method)))
         "thingy"
         "this is bogus"
         ""))

(testing "slash"
  (is (:success (slash "/"))))
(comment testing "URI"
  (are [path] (:success (uri path))
       "/rfc/rfc1808.txt"
       "/00/Weather/California/Los%20Angeles"
       "/faq/compression-faq/part1.html"))

(comment testing "Request Line"
  (are [line expected] (= expected (:matches (request-line line)))
       "GET / HTTP/1.1\r\n" ["GET" "/" "HTTP/1.1"]))
