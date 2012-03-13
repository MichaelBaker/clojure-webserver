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
