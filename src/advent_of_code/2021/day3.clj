(ns advent-of-code.2021.day3
  (:require [clojure.string :as string]))

(defn part-1 []
  (->> (slurp "./resources/2021/day3-input")
       (string/split-lines)
       (map (comp (partial apply hash-map)
                  (partial interleave (range 12))
                  vec))
       (apply (partial merge-with (comp flatten vector)))
       (map (fn [[k v]] [k (frequencies v)]))
       (sort-by first)
       (reduce (fn [result [index {freq-0 \0 freq-1 \1}]]
                 (-> result
                     (update :gamma-bits  + (if (> freq-0 freq-1) 0 (Math/pow 2 (Math/abs (float (- 11 index))))))
                     (update :epslon-bits + (if (> freq-0 freq-1) (Math/pow 2 (Math/abs (float (- 11 index)))) 0))))
               {:gamma-bits  0
                :epslon-bits 0})
       vals
       (apply *)))


