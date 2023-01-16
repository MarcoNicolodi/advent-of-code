(ns advent-of-code.2022.day6)


(defn part-1 []
  (let [input-stream
        (clojure.java.io/input-stream
          (clojure.java.io/file "./resources/2022/day6-input"))]
    (loop [window [(.read input-stream)
                   (.read input-stream)
                   (.read input-stream)
                   (.read input-stream)]
           cursor-position 4]
      (if (= 4 (count (into #{} window)))
        cursor-position
        (recur (conj (vec (drop 1 window)) (.read input-stream))
               (inc cursor-position))))))