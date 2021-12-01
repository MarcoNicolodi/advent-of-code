(ns advent-of-code.day2
  (:require [clojure.string :as str]))

;;part 1
(def path "./resources/2020/day2-input")

(defn wire->policy+password [path]
  (->> path
       slurp
       str/split-lines
       (map (comp (fn [m] (-> m
                              (update :min #(Integer/parseInt %))
                              (update :max #(Integer/parseInt %))))
                  (partial apply hash-map)
                  (partial interleave [:min :max :letter :password])
                  rest
                  (partial re-find #"(\d+)-(\d+)\s(\w):\s(\w+)")))))

(defn parse-passwords [policy+password]
  (let [freqs (->  policy+password :password (str/split #"") frequencies)]
    (and (>= (get freqs (:letter policy+password) 0) (:min policy+password))
         (<= (get freqs (:letter policy+password) 0) (:max policy+password)))))

(defn result [path]
  (->> path
       wire->policy+password
       (map parse-passwords)
       (filter true?)
       count))

;;part 2

(defn wire->policy+password [path]
  (->> path
       slurp
       str/split-lines
       (map (comp (fn [m] (-> m
                              (update :pos1 #(Integer/parseInt %))
                              (update :pos2 #(Integer/parseInt %))))
                  (partial apply hash-map)
                  (partial interleave [:pos1 :pos2 :letter :password])
                  rest
                  (partial re-find #"(\d+)-(\d+)\s(\w):\s(\w+)")))))

(defn parse-passwords [policy+password]
  (= 1 (count (filter (partial = (:letter policy+password))
                      ((juxt (fn [letters] (str (nth letters (dec (:pos1 policy+password)))))
                             (fn [letters] (str (nth letters (dec (:pos2 policy+password))))))
                       (:password policy+password))))))

(defn result [path]
  (->> path
       wire->policy+password
       (map parse-passwords)
       (filter true?)
       count))