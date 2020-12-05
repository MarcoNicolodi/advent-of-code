(ns advent-of-code-2020.day5
  (:require [clojure.string :as str]))

(def path "./resources/day5-input")
(def rows 127)
(def seats 7)

(defn line->row+column-paths [line]
  (partition-all 7 line))

(defn row+column-paths->binary-search-directions
  [[row column]]
  (let [row-ops    {\F :lower-half \B :upper-half}
        column-ops {\R :upper-half \L :lower-half}]
    (println row column)
    [(map row-ops row)
     (map column-ops column)]))

(defn wire->row+column+paths [path]
  (->> path
       slurp
       str/split-lines
       (map (comp row+column-paths->binary-search-directions
                  line->row+column-paths))))

(defn binary-search [directions num]
  (loop [[direction & rest] directions
         low         0
         high        num]
    (if-not direction
      high
      (if (= direction :upper-half)
        (recur rest (int (Math/floor (/ (+ low high) 2))) high)
        (recur rest low (int (Math/floor (/ (+ low high) 2))))))))

(defn seat-id [[row seat]]
  (+ seat (* row 8)))

(defn find-row+seat [[row-directions column-directions]]
  [(binary-search row-directions rows)
   (binary-search column-directions seats)])

(defn find-greatest-seat-code [path]
  (->> path
       wire->row+column+paths
       (map (comp seat-id find-row+seat))
       (apply max)))

(comment
  (find-greatest-seat-code path))