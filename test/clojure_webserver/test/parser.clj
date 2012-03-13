(ns clojure-webserver.test.parser
  (:use [clojure-webserver.parser]),
  (:use [clojure.test]))

(testing "word-parser"
  (deftest matches-beginning-of-string
    (let [parser (word-parser "te")]
      (is (= ["te"] (:matches (parser "test")))))))

(testing "choice"
  (deftest matches-either-parser
    (let [p1 (word-parser "te")
          p2 (word-parser "ab")
          parser (choice p1 p2)]
      (is (= ["te"] (:matches (parser "test"))))
      (is (= ["ab"] (:matches (parser "abba")))))))

(testing "sequence"
  (deftest matches-two-tokens-in-order
    (let [p1 (word-parser "te")
          p2 (word-parser "ab")
          parser (>> p1 p2)]
      (is (= ["te" "ab"] (:matches (parser "teab")))))))

(testing "space"
  (deftest matches-the-space-character
    (is (= [" "] (:matches (space " "))))))

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

(testing "Ignore"
  (deftest it-returns-success-and-consumes-token-but-no-matches
    (let [parser (ignore (word-parser "te"))]
      (is (= {:matches [] :remaining "st" :success true} (parser "test"))))))

(testing "Request Line"
  (are [line expected] (= expected (:matches (request-line line)))
       "GET / HTTP/1.1\r\n" ["GET" "/" "HTTP/1.1"]))
