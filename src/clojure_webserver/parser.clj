(ns clojure-webserver.parser
  (:require [clojure.string :as string]))

; This is not technically a parser given the below definition
(defn -match-character [c]
  (fn [string]
    (if (= (first string) c)
      {:matches [(str (first string))] :remaining (string/join "" (rest string)) :success true}
      {:matches [] :remaining string :success false})))

; Parsers are functions of the form String -> {:matches [String] :remaining String :success Boolean}
; where :matches   is a list containing the matched token
;       :remaining is the rest of the input string
;       :success   is whether or not the parse was successful
(defn choice [& parsers]
  (defn try-parsers [string ps]
    (if (seq ps)
      (let [{:keys [matches remaining success] :as result} ((first ps) string)]
        (if success
          result
          (recur string (rest ps))))
      {:matches [] :remaining string :success false}))
  (fn [string] (try-parsers string parsers)))

(defn >> [& parsers]
  (defn new-result [{m1 :matches r1 :remaining} {m2 :matches r2 :remaining}]
    {:matches (into m2 m1) :remaining r1 :success true})
  (defn try-sequence [original-string substring ps final-result]
    (if (seq ps)
      (let [{:keys [matches remaining success] :as result} ((first ps) substring)]
        (if success
          (try-sequence original-string remaining (rest ps) (new-result result final-result))
          {:matches [] :remaining original-string :success false}))
      final-result))
  (fn [string] (try-sequence string string parsers {:matches [] :remaining string :success true})))

(defn ignore [parser]
  (fn [string] (assoc (parser string) :matches [])))

(defn regex-parser [re]
  (let [pattern (re-pattern (str "^" re))]
    (fn [string]
      (let [match (re-find pattern string)]
        (if match
          {:matches [match] :remaining (string/replace-first string match "") :success true}
          {:matches [] :remaining string :success false})))))

(def lowercase-character (regex-parser #"[a-z]"))
(def uppercase-character (regex-parser #"[A-Z]"))
(def digit (regex-parser #"[0-9]"))
(def alpha-numeric (choice lowercase-character uppercase-character digit))

(defn many [parser]
  (defn repeat-parser [string result]
    (let [{:keys [matches remaining success]} (parser string)
          {previous :matches} result]
      (if success
        (recur remaining {:matches (into previous matches)
                          :remaining remaining
                          :success true})
        result)))
  (fn [string] (repeat-parser string {:matches [] :remaining string :success true})))

(defn join [parser]
  (fn [string]
    (let [{:keys [matches success] :as result} (parser string)]
      (if success
        (assoc result :matches [(apply str matches)])
        result))))

(defn word-parser [word]
  (join (apply >> (map -match-character (seq word)))))

(def space (word-parser " "))
