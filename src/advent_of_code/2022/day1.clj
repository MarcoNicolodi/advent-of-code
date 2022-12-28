(ns advent-of-code.2022.day1)

(def calory-sum
  (->> (slurp "./resources/2022/day1-input")
       (clojure.string/split-lines)
       (partition-by empty?)
       (remove (comp empty? first))
       (map (comp (partial reduce +)
                  (partial map parse-long)))))

(def part-1 (apply max calory-sum))
(def part-2 (reduce + (take 3 (sort > calory-sum))))