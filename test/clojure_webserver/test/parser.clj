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

(testing "Ignore"
  (deftest it-returns-success-and-consumes-token-but-no-matches
    (let [parser (ignore (word-parser "te"))]
      (is (= {:matches [] :remaining "st" :success true} (parser "test"))))))

(testing "digit"
  (deftest matches-any-digit
    (are [d] (:success (digit d))
         "0" "1" "2" "3" "4" "5" "6" "7" "8" "9")))

(testing "lowercase-character"
  (deftest matches-any-lowercase-english-character
    (are [c] (:success (lowercase-character c))
         "a" "b" "c" "d" "e" "f" "g" "h" "i" "j" "k" "l" "m" "n" "o" "p" "q" "r" "s" "t" "u" "v" "w" "x" "y" "z")))

(testing "uppercase-character"
  (deftest matches-any-uppercase-english-character
    (are [c] (:success (uppercase-character c))
         "A" "B" "C" "D" "E" "F" "G" "H" "I" "J" "K" "L" "M" "N" "O" "P" "Q" "R" "S" "T" "U" "V" "W" "X" "Y" "Z")))
