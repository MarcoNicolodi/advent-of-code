(ns advent-of-code-2020.day11
  (:require [clojure.string :as str]))

(def path "./resources/day11-input")

(def char->object
  {"." :floor
   "L" :empty-seat
   "#" :occupied-seat})

(defn wire->internal [path]
  (->> path slurp str/split-lines (mapv (comp (partial mapv char->object) #(str/split % #"")))))

(defn swipe? [x y adjacent-seats {:keys [seating-places]}])

(defn swipe [x y new-object state])

(defn adjacent-seats [x y {:keys [seating-places width height]}])

(defn new-state [seating-places])

(defn count-occupied-seats [state])

(defn settle-seating-places [seating-places]
  (loop [state (new-state seating-places)]
    (if (:settled state)
      (count-occupied-seats state)
      (reduce
        (fn [[x state] column]
          (reduce
            (fn [[y state] seat])
            [y column]))
        [0 state]
        (:seating-places state)))))
