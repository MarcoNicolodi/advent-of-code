(ns advent-of-code-2020.day9
  (:require [clojure.string :as str]))

(def path "./resources/day9-input")
(def preamble 25)

(defn wire->internal [path]
  (->> path
       slurp
       str/split-lines
       (map #(Long/parseLong %))))

(defn has-pair-that-sums-to? [coll value]
  (some (partial = :valid)
        (for [a coll
              b coll
              :when (and (not= a b) (= (+ a b) value))]
          :valid)))

(defn find-invalid [nums preamble]
  (loop [i 0]
    (let [batch (take (inc preamble) (drop i nums))
          subject (last batch)
          sample (butlast batch)
          valid? (has-pair-that-sums-to? sample subject)]
      (if valid?
        (recur (inc i))
        subject))))

(def part1 []
  (find-invalid (wire->internal path) preamble))