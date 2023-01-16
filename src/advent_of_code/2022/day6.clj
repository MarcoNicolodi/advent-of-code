(ns advent-of-code.2022.day6)


(defn runner [window-size]
  (let [input-stream
        (clojure.java.io/input-stream
          (clojure.java.io/file "./resources/2022/day6-input"))]
    (loop [window (vec (repeatedly window-size (fn [] (.read input-stream))))
           cursor-position window-size]
      (if (= window-size (count (into #{} window)))
        cursor-position
        (recur (conj (vec (drop 1 window)) (.read input-stream))
               (inc cursor-position))))))

(def part-1 (runner 4))
(def part-1 (runner 14))