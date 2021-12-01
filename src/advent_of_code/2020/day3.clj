(ns advent-of-code.2020.day3
  (:require [clojure.string :as str]))

(def path "./resources/2020/day3-input")

(defn wire-vec->map [map-vec]
  {:path map-vec
   :width (-> map-vec first count)
   :height (count map-vec)})

(defn wire->map [path]
  (->> path
       slurp
       str/split-lines
       (map #(str/split % #""))
       wire-vec->map))

(defn new-state []
  {:down 0
   :right 0
   :tree-count 0
   :arrived? false})

(defn tree? [map' state]
  (-> map'
      :path
      (nth (:down state))
      (nth (:right state))
      (= "#")))

(defn move-right [map' state]
  (if (:arrived? state)
    state
    (update state :right #(mod (inc %) (:width map')))))

(defn move-down [map' state]
  (if (:arrived? state)
    state
    (let [new-location (update state :down inc)]
      (if (= (:down new-location) (dec (:height map')))
        (assoc new-location :arrived? true)
        new-location))))

(defn down-the-slope [state slope map']
  (reduce (fn [acc-state move] (move acc-state))
          state
          (flatten [(take (:right slope) (repeat (partial move-right map')))
                    (take (:down slope) (repeat (partial move-down map')))])))

(defn navigate [map' slope]
  (loop [state (new-state)]
    (let [new-location (down-the-slope state slope map')]
      (let [new-state (if (tree? map' new-location)
                        (update new-location :tree-count inc)
                        new-location)]
        (if (:arrived? new-state)
          new-state
          (recur new-state))))))

(comment
  (navigate (wire->map path) {:right 3 :down 1})

  (->>
    [{:right 1 :down 1}
     {:right 3 :down 1}
     {:right 5 :down 1}
     {:right 7 :down 1}
     {:right 1 :down 2}]
    (map (comp :tree-count (partial navigate (wire->map path))))
    (reduce *)))