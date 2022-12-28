(ns advent-of-code.2021.day2
  (:require [clojure.string :as string]))

(def path "./resources/2021/day2-input")

(defn depth*horizontal [path]
  (->> (slurp path)
       (string/split-lines)
       (reduce (fn [result current]
                 (let [[direction value] (string/split current #" ")
                       value (Integer/parseInt value)]
                   (case direction
                         "forward" (update result :horizontal + value)
                         "up"      (update result :depth - value)
                         "down"    (update result :depth + value)))
                 )
               {:horizontal 0
                :depth 0})
       vals
       (apply *)))

(defn depth*horizontal [path]
  (let [{:keys [horizontal depth]} (->> (slurp path)
                                        (string/split-lines)
                                        (reduce (fn [result current]
                                             (let [[direction value] (string/split current #" ")
                                                   value (Integer/parseInt value)]
                                               (case direction
                                                 "forward" (-> result
                                                               (update :horizontal + value)
                                                               (update :depth + (* value (- (:aim result)))))
                                                 "up" (update result :aim + value)
                                                 "down" (update result :aim - value)))
                                             )
                                           {:aim        0
                                            :horizontal 0
                                            :depth      0}))]

    (* horizontal depth)))

