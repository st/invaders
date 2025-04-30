(ns st.invaders.pattern)

(def signal-cell \o)
(def empty-cell \-)

(defn signal-cell?
  [cell]
  (= signal-cell cell))

(defn noise-cell?
  [cell]
  (not (#{signal-cell empty-cell} cell)))

(defn cells-matches?
  [grid-cell pattern-cell]
  (or
    (signal-cell? grid-cell)
    (noise-cell? grid-cell)
    (= grid-cell pattern-cell)))

(defn detect-pattern?
  "grid and pattern have the same dimension"
  [grid pattern]
  (let [grid-data (flatten grid)
        pattern-data (flatten pattern)]
    (->> (map (fn [grid-cell pattern-cell] (cells-matches? grid-cell pattern-cell))
              grid-data pattern-data)
         (every? true?))))
