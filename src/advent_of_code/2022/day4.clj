(ns advent-of-code.2022.day4
  (:require [clojure.string :as string]))

(def input (clojure.string/split-lines
             (slurp "./resources/2022/day4-input")))

(defn line->assignment-pair
  [line]
  (map (comp
         (partial map parse-long)
         #(string/split % #"-"))
       (string/split line #",")))

(defn lines->assignment-pairs
  [lines]
  (map line->assignment-pair lines))

(defn fully-contains?
  [assignment-pair]
  (let [start [(ffirst assignment-pair) (first (second assignment-pair))]
        end   [(second (first assignment-pair)) (second (second assignment-pair))]]
    (or (and (<= (first start) (second start))
             (>= (first end)   (second end)))
        (and (<= (second start) (first start))
             (>= (second end)   (first end))))))

(def part-1
  (->> input
       lines->assignment-pairs
       (filter fully-contains?)
       count))