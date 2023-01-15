(ns advent-of-code.2022.day5
  (:require [taoensso.timbre :refer [spy]]))

;; input parser
(def input (clojure.string/split-lines
             (slurp "./resources/2022/day5-input")))

;;;; stack parser
(defn identify-stack-numeration-line
  [input]
  (loop [[line & lines] input
         line-number 1]
    (if (re-matches #"^\s\d.*" line)
      line-number
      (recur lines (inc line-number)))))

(defn identify-stack-amount
  [input, stack-numeration-line-number]
  (let [stack-numeration-line (first (drop (dec stack-numeration-line-number) input))]
    (parse-long (str (last stack-numeration-line)))))

(defn stack-content-horizontal-position
  [stack-number]
  (+ 1 (*  4 (- stack-number 1))))

(defn parse-stacks
  [input]
  (let [stack-numeration-line (identify-stack-numeration-line input)
        stack-amount (identify-stack-amount input stack-numeration-line)
        empty-stacks (apply merge (for [i (range 1 (inc stack-amount))]
                                    (hash-map i (list))))]
    (reduce (fn [stacks stack-number]
           (let [stack-content-horizontal-position (stack-content-horizontal-position stack-number)]
             (loop [line-number (dec (dec stack-numeration-line))
                    stack (list)]
               (if (< line-number 0)
                 (assoc stacks stack-number stack)
                 (recur (dec line-number)
                        (let [stack-content (if (< (count (nth input line-number)) stack-content-horizontal-position)
                                              \space
                                              (nth (nth input line-number) stack-content-horizontal-position))]
                         (if-not (= \space stack-content)
                           (conj stack stack-content)
                           stack)))))))
            empty-stacks
            (range 1 (inc stack-amount)))))

;;;; command parser
(defn identify-command-start-line
  [input]
  (+ (identify-stack-numeration-line input) 2))

(defn parse-command
  [line]
  (let [[_ amount origin target] (re-matches #".*(\d).*(\d).*(\d)" line)]
    {:amount (parse-long amount) :origin (parse-long origin) :target (parse-long target)}))

(defn parse-commands
  [input]
  (let [command-start-line (identify-command-start-line input)
        command-lines (drop (dec command-start-line) input)]
    (mapv parse-command command-lines)))


;; run commands
(defn run-command
  [command stacks]
  (println stacks)
  (loop [i (:amount command)
         stacks stacks]
    (println i)
    (if (= 0 i)
      stacks
      (let [{:keys [origin target]} command
            origin-stack (get stacks origin)
            target-stack (get stacks target)
            crate (peek origin-stack)
            new-origin-stack (pop origin-stack)
            new-target-stack (conj target-stack crate)
            new-stacks (assoc stacks origin new-origin-stack target new-target-stack)]
        (recur (dec i) new-stacks)))))

(defn part-1
  [input]
  (let [stacks (parse-stacks input)
        commands (parse-commands input)]
    (loop [stacks stacks
           [command & commands] commands]
      (println command)
      (if-not command
        stacks
        (recur (run-command command stacks) commands)))))