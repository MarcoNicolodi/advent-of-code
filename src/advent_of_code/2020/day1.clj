(ns advent-of-code.day1)

(def input
  (->> (slurp "./resources/2020/day1-input")
       (clojure.string/split-lines)
       (map #(Integer/parseInt %))))

(defn result-part-1 [input]
  (loop [[head & tail] input]
    (or (loop [[x & xs] tail]
          (when x
            (if (= (+ head x) 2020)
              (* head x)
              (recur xs))))
        (recur tail))))

(defn result-part-2 [input]
  (loop [[head & tail] input]
    (or
      (loop [[x & xs] tail]
        (when x
          (or
            (loop [[y & ys] xs]
              (when y
                (if (= (+ head x y) 2020)
                  (* head x y)
                  (recur ys))))
            (recur xs))))
      (recur tail))))