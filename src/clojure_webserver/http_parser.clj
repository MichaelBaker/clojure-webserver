(ns clojure-webserver.http_parser
  (:use clojure-webserver.parser))

(def request-method
  (choice (word-parser "OPTIONS")
          (word-parser "GET")
          (word-parser "HEAD")
          (word-parser "POST")
          (word-parser "PUT")
          (word-parser "DELETE")
          (word-parser "TRACE")
          (word-parser "CONNECT")))

(def uri
  (word-parser "Hello"))

(def request-line
  (>> request-method
      ignore-space))
