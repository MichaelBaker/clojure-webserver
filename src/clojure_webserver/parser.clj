(ns clojure-webserver.parser
  (:use [clojure.string :only [replace-first]]))

; Parsers are functions of the form String -> {:matches [String] :remaining String :success Boolean}
; where :matches   is a list containing the matched token
;       :remaining is the rest of the input string
;       :success   is whether or not the parse was successful
(defn word-parser [re]
  (let [pattern (re-pattern (str "^" re))]
    (fn [string]
      (let [match (re-find pattern string)]
        (if match
          {:matches [match] :remaining (replace-first string match "") :success true}
          {:matches [] :remaining string :success false})))))

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

(def space (word-parser " "))
(defn ignore [parser]
  (fn [string] (assoc (parser string) :matches [])))

(defn regex-parser [re]
  (let [pattern (re-pattern (str "^" re))]
    (fn [string]
      (let [match (re-find pattern string)]
        (if match
          {:matches [match] :remaining (replace-first match string "") :success true}
          {:matches [] :remaining string :success false})))))

(def lowercase-character (regex-parser #"[a-z]"))
(def uppercase-character (regex-parser #"[A-Z]"))
(def digit (regex-parser #"[0-9]"))
(def alpha-numeric (choice lowercase-character uppercase-character digit))
