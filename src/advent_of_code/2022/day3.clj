(ns advent-of-code.2022.day3
  (:require [clojure.string :as string]
            [clojure.set :as set]))

(def input (clojure.string/split-lines
             (slurp "./resources/2022/day3-input")))

(defn input->compartment-items
  [input]
  [(set (take (/ (count input) 2) input))
   (set (drop (/ (count input) 2) input))])

(defn item->priority
  [item]
  (if (>= (int item) 97)
    (- (int item) 96)
    (- (int item) 38)))

(def part-1
  (->> input
       (map (comp item->priority
                  first
                  (partial apply set/intersection)
                  input->compartment-items))
       (reduce +)))
