(ns advent-of-code-2020.day4
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(def path "./resources/day4-input")

(def required-data
  #{"byr" "iyr" "eyr" "hgt" "hcl" "ecl" "pid"})

(defn valid-byr? [byr]
  (<= 1920 (Integer/parseInt byr) 2002))

(defn valid-iyr? [iyr]
  (<= 2010 (Integer/parseInt iyr) 2020))

(defn valid-eyr? [eyr]
  (<= 2020 (Integer/parseInt eyr) 2030))

(defn valid-hgt? [hgt]
  (when-let [parsed (re-find #"^(\d+)(cm|in)$" hgt)]
    (let [size (Integer/parseInt (second parsed))
          measure (second (rest parsed))]
      (if (= measure "cm")
        (<= 150 size 193)
        (<= 59 size 76)))))

(defn valid-hcl? [hcl]
  (re-find #"^#([0-9a-f]{6})$" hcl))

(defn valid-ecl? [ecl]
  (#{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"} ecl))

(defn valid-pid? [pid]
  (re-find #"^[0-9]{9}$" pid))

(defn valid-cid? [cid] true)

(def field->validation
  {"byr" valid-byr?
   "iyr" valid-iyr?
   "eyr" valid-eyr?
   "hgt" valid-hgt?
   "hcl" valid-hcl?
   "ecl" valid-ecl?
   "pid" valid-pid?
   "cid" valid-cid?})

(defn wire->passports [path]
  (-> path
      slurp
      (str/split #"\n\s+")
      (->> (map (comp (partial apply hash-map)
                      #(str/split % #" ")
                      #(str/replace % ":" " ")
                      #(str/replace % "\n" " "))))))

(defn has-required-data? [required-data passport]
  (empty? (set/difference required-data (-> passport keys set))))

(defn valid-data? [field->validation passport]
  (reduce-kv (fn [_ field value] (if ((field->validation field) value)
                                   true
                                   (reduced false)))
             true
             passport))

(defn count-valid [path]
  (->> path
      wire->passports
      (filter (fn [passport] (and (has-required-data? required-data passport)
                                  (valid-data? field->validation passport))))
      count))

(comment
  (count-valid path))