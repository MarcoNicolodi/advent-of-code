(ns advent-of-code-2020.day10
  (:require [clojure.string :as str]))

(def path "./resources/day10-input")

(defn wire->internal [path]
  (->> path slurp str/split-lines (map #(Integer/parseInt %))))

(defn sorted-vec [in-seq] (-> in-seq sort vec))

(defn with-charging-outlet-joltage [in] (->> in (cons 0) vec))

(defn with-build-in-adapter-joltage [in] (-> in sort vec (conj (+ 3 (last in)))))

(defn diffs [in]
  (group-by identity (for [i (range (count in))
                           :when (not= i (dec (count in)))]
                       (- (nth in (inc i)) (nth in i)))))

(defn *-3s-and-1s-counts [diffs]
  (* (count (get diffs 3)) (count (get diffs 1))))

(def part1 (comp *-3s-and-1s-counts diffs with-build-in-adapter-joltage with-charging-outlet-joltage sorted-vec wire->internal))