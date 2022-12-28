(ns advent-of-code.2022.day2
  (:require [clojure.string :as string]))

(def input (clojure.string/split-lines
             (slurp "./resources/2022/day2-input")))

(def left-input->play
  {"A" :rock
   "B" :paper
   "C" :scissor})

(def right-input->play
  {"X" :rock
   "Y" :paper
   "Z" :scissor})

(def right-input->desired-outcome
  {"X" :defeat
   "Y" :draw
   "Z" :victory})

(def outcome->points
  {:defeat 0
   :draw 3
   :victory 6})

(def play->points
  {:rock 1
   :paper 2
   :scissor 3})

(defn input->game-round [input]
  (let [[left right] (string/split input #" ")]
    [(left-input->play left)
    (right-input->play right)]))

(defn input->left+desired-outcome [input]
  (let [[left right] (string/split input #" ")]
    [(left-input->play left)
     (right-input->desired-outcome right)]))

(defn input->game-rounds [input]
  (map input->game-round input))

(defn input->lefts+desired-outcomes [input]
  (map input->left+desired-outcome input))

(defn round->outcome [round]
  (let [[left right] round]
   (cond
     (= left right)
     :draw

     (and (= :rock left) (= :scissor right))
     :defeat

     (and (= :rock left) (= :paper right))
     :victory

     (and (= :paper left) (= :rock right))
     :defeat

     (and (= :paper left) (= :scissor right))
     :victory

     (and (= :scissor left) (= :paper right))
     :defeat

     (and (= :scissor left) (= :rock right))
     :victory)))

(defn round->score [round]
  (let [outcome (round->outcome round)
        play (second round)
        score (+ (outcome->points outcome)
                 (play->points play))]
    score))

(defn left+desired-outcome->left+matching-right
  [[left desired-outcome]]
  (let [possible-plays [[left :paper] [left :rock] [left :scissor]]
        possible-plays+outcomes (map (juxt identity round->outcome) possible-plays)
        matching-round (first (first (filter #(= desired-outcome (second %)) possible-plays+outcomes)))
        matching-right (second matching-round)]
    [left matching-right]))

(def part-1 (->> input
                 input->game-rounds
                 (map round->score)
                 (reduce +)))

(def part-2 (->> input
                 input->lefts+desired-outcomes
                 (map left+desired-outcome->left+matching-right)
                 (map round->score)
                 (reduce +)))
