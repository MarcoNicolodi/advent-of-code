(ns advent-of-code.2022.day5)

;; input parser
(def input (clojure.string/split-lines
             (slurp "./resources/2022/day5-input")))

;;;; stack parser, create a hashmap of stacks
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

(defn stack-parser
  [input]
  (let [stack-numeration-line (identify-stack-numeration-line input)
        stack-amount (identify-stack-amount input stack-numeration-line)]
    (map (fn [stack-number]
           (let [stack-content-horizontal-position (stack-content-horizontal-position stack-number)]
             (loop [line-number (dec (dec stack-numeration-line))
                    stack (list)]
               (if (< line-number 0)
                 stack
                 (recur (dec line-number)
                        (let [stack-content (if (< (count (nth input line-number)) stack-content-horizontal-position)
                                              \space
                                              (nth (nth input line-number) stack-content-horizontal-position))]
                         (if-not (= \space stack-content)
                           (conj stack stack-content)
                           stack)))))))
      (range 1 (inc stack-amount)))))


;;;; command parser
;; run commands