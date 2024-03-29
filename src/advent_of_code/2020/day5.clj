(ns advent-of-code.2020.day5
  (:require [clojure.string :as str]))

(def path "./resources/2020/day5-input")
(def rows 127)
(def seats 7)

(defn line->row+column-directions [line]
  (partition-all 7 line))

(defn row+column-paths->binary-search-directions
  [[row column]]
  (let [row-ops    {\F :lower-half \B :upper-half}
        column-ops {\R :upper-half \L :lower-half}]
    (println row column)
    [(map row-ops row)
     (map column-ops column)]))

(defn wire->rows+columns+directions [path]
  (->> path
       slurp
       str/split-lines
       (map (comp row+column-paths->binary-search-directions
                  line->row+column-directions))))

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

(defn flight-seat-ids [path]
  (->> path
       wire->rows+columns+directions
       (map (comp seat-id find-row+seat))))

(defn find-greatest-seat-id [seat-ids]
  (apply max seat-ids))

(defn find-missing-seat-id
  [seat-ids]
  (let [sorted-ids (set (sort seat-ids))
        all-seat-ids (set (range (apply min sorted-ids) (inc (apply max sorted-ids))))]
    (clojure.set/difference all-seat-ids sorted-ids)))

(comment
  (let [seat-ids (flight-seat-ids path)]
    ((juxt find-greatest-seat-id find-missing-seat-id) seat-ids)))