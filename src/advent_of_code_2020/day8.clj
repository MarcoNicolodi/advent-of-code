(ns advent-of-code-2020.day8
  (:require  [clojure.string :as str]
             [clojure.set    :as set]))

(def path "./resources/day8-input")

(def wire-op->internal->op
  {"acc" :accumulate
   "nop" :noop
   "jmp" :jump})

(defn accumulate [state value]
  (-> state
      (update :accumulated (partial + value))
      (update :ran-ops     #(conj % (:position state)))
      (update :position    inc)))

(defn noop [state _]
  (-> state
      (update :ran-ops     #(conj % (:position state)))
      (update :position    inc)))

(defn jump [state value]
  (-> state
      (update :ran-ops     #(conj % (:position state)))
      (update :position    (partial + value))))

(def op->fn
  {:accumulate accumulate
   :noop       noop
   :jump       jump})


(defn wire->internal [[wire-op wire-value]]
  (vector (wire-op->internal->op wire-op) (Integer/parseInt wire-value)))

(defn wire->op+value [path]
  (->> path
       slurp
       str/split-lines
       (map (comp wire->internal #(str/split % #" ")))))

(defn new-state []
  {:position    0
   :accumulated 0
   :ran-ops     #{}})

(defn run-ops [ops+values]
  (loop [state (new-state)]
    (if ((:ran-ops state) (:position state))
      (:accumulated state)
      (let [[op value] (nth ops+values (:position state))]
        (recur ((op->fn op) state value))))))

(def part1 (comp run-ops wire->op+value))