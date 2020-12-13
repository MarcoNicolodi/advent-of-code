(ns advent-of-code-2020.day7
  (:require [clojure.string :as str]))

(def path "./resources/day7-input")

(defn wire-line->bag-info
  [line]
  (let [[bag content] (str/split line #"contain")]
    [(re-find #"\w+\s\w+\sbag" bag)
     (->> content
          (re-seq #"(\d+)\s(\w+\s\w+\sbag)")
          (mapcat rest)
          reverse
          (apply hash-map))]))

(defn wires->bags-infos [path]
  (->> path
       slurp
       (str/split-lines)
       (mapcat wire-line->bag-info)
       (apply hash-map)))