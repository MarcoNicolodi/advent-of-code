(ns advent-of-code-2020.day6
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(def path "./resources/2020/day6-input")

(defn part1 [path]
  (-> path
       slurp
      (str/split #"\n\n")
      (->> (map (comp count
                      distinct
                      #(str/replace % "\n" "")))
           (reduce +))))

(defn wire->group-personal-responses [path]
  (-> path
      slurp
      (str/split #"\n\n")
      (->> (map #(str/split % #"\n")))))

(defn questions-everyone-answered [group-response]
  (->> group-response
       (map set)
       (apply set/intersection)))

(defn part2 [path]
  (->> path
      wire->group-personal-responses
      (map (comp count questions-everyone-answered))
      (reduce +)))