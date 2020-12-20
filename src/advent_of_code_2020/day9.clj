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

(defn part1 []
  (find-invalid (wire->internal path) preamble))

(defn contiguous-set-that-sums-to [nums value]
  (loop [i 0]
    (or (reduce (fn [contiguous-set x]
               (let [sum (apply + contiguous-set)]
                 (if (> sum value)
                   (reduced false)
                   (if (= value (+ sum x))
                     (reduced (conj contiguous-set x))
                     (conj contiguous-set x)))))
             []
             (drop i nums))
        (recur (inc i)))))

(defn part-2 []
  (let [in (wire->internal path)
        min-and-max (juxt (partial apply min) (partial apply max))
        contiguous-set (contiguous-set-that-sums-to in (find-invalid in preamble))]
    (apply + (min-and-max contiguous-set))))

