(ns advent-of-code.2020.day8
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(def path "./resources/2020/day8-input")

(def wire-op->internal->op
  {"acc" :accumulate
   "nop" :noop
   "jmp" :jump})

(defn accumulate [state value]
  (-> state
      (update :accumulated (partial + value))
      (update :ran-ops #(conj % (:position state)))
      (update :position inc)))

(defn noop [state _]
  (-> state
      (update :ran-ops #(conj % (:position state)))
      (update :position inc)))

(defn jump [state value]
  (-> state
      (update :ran-ops #(conj % (:position state)))
      (update :position (partial + value))))

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

(defn infinite-loop? [state]
  ((:ran-ops state) (:position state)))

(defn last-op? [state ops+values]
  (let [[_ _ last?] (nth ops+values (:position state))]
    last?))

(def op->switch-op
  {:jump :noop
   :noop :jump})

(defn flag-last-op [ops+values]
  (-> ops+values
      vec
      (subvec 0 (dec (count ops+values)))
      (conj (conj (last ops+values) :last-op))))

(defn next-switchable-op-idx [ops+values]
  (reduce (fn [idx [op & _]]
            (if (op->switch-op op)
              (reduced idx)
              (inc idx)))
          0
          ops+values))

(defn switch-next-switchable-op [ops+values {idx :last-switched-op-idx :as state}]
  (if idx
    (let [before-switch (subvec ops+values 0 idx)
          last-switched-op (nth ops+values idx)
          after-switch (vec (drop (inc idx) ops+values))
          undo-last-switch (update last-switched-op 0 op->switch-op)
          next-switchable-index (next-switchable-op-idx after-switch)
          switched (update after-switch next-switchable-index (fn [op+value] (update op+value 0 op->switch-op)))
          new-ops+values (concat (conj before-switch undo-last-switch) switched)
          new-state (assoc (new-state) :last-switched-op-idx (+ (:last-switched-op-idx state) (inc next-switchable-index)))]
      [new-ops+values new-state])
    (let [next-switchable-index (next-switchable-op-idx ops+values)
          new-ops+values (update ops+values next-switchable-index (fn [op+value] (update op+value 0 op->switch-op)))
          new-state (assoc (new-state) :last-switched-op-idx next-switchable-index)]
      [new-ops+values new-state])))

(defn run-ops-with-failure-switching [ops+values]
  (loop [ops+values (flag-last-op ops+values)
         state (new-state)]
    (if (infinite-loop? state)
      (let [[switched-ops+values state] (switch-next-switchable-op (vec ops+values) state)]
        (recur switched-ops+values state))
      (if (last-op? state ops+values)
        (:accumulated state)
        (let [[op value] (nth ops+values (:position state))]
         (recur ops+values ((op->fn op) state value)))))))

(def part2 (comp run-ops-with-failure-switching wire->op+value))