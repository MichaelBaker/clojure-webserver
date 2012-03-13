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

; abs_path      = "/"  path_segments
; path_segments = segment *( "/" segment )
; segment       = *pchar *( ";" param )
; param         = *pchar
; pchar         = unreserved | escaped | ":" | "@" | "&" | "=" | "+" | "$" | ","
; unreserved    = alphanum | mark
; escaped       = "%" hex hex
; hex           = digit | "A" | "B" | "C" | "D" | "E" | "F" |
;                         "a" | "b" | "c" | "d" | "e" | "f"
; mark          = "-" | "_" | "." | "!" | "~" | "*" | "'" | "(" | ")"
; alphanum      = alpha | digit
; alpha         = lowalpha | upalpha
; lowalpha = "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h" | "i" |
;            "j" | "k" | "l" | "m" | "n" | "o" | "p" | "q" | "r" |
;            "s" | "t" | "u" | "v" | "w" | "x" | "y" | "z"
; upalpha  = "A" | "B" | "C" | "D" | "E" | "F" | "G" | "H" | "I" |
;            "J" | "K" | "L" | "M" | "N" | "O" | "P" | "Q" | "R" |
;            "S" | "T" | "U" | "V" | "W" | "X" | "Y" | "Z"
; digit    = "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" |
;            "8" | "9"
(def slash (word-parser "/"))
(def uri
  (word-parser "Hello"))

(def ignore-space (ignore space))

(def request-line
  (>> request-method
      ignore-space))
