(ns advent-of-code.2021.day1)

(def input
  (->> (slurp "./resources/2021/day1-input")
       (clojure.string/split-lines)
       (map #(Integer/parseInt %))))



(defn part-1 [input]
  (let [[increases _] (reduce (fn [[increases latest] curr]
                                (if (> curr latest)
                                  [(inc increases) curr]
                                  [increases curr]))
                              [0 (first input)]
                              (rest input))]
    increases))

(defn sliding-window [n coll]
  (->> coll
    (map-indexed (fn [i _] (->> input (drop i) (take n))))
    (take (- (count coll) (dec n)))))

(defn part-2 [input]
  (->> input
       (sliding-window 3)
       (map (partial reduce + 0))
       part-1))