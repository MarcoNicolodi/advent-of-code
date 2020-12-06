(ns advent-of-code-2020.day6
  (:require [clojure.string :as str]))

(def path "./resources/day6-input")

(defn part1 [path]
  (-> path
       slurp
      (str/split #"\n\n")
      (->> (map (comp count
                      distinct
                      #(str/replace % "\n" "")))
           (reduce +))))

