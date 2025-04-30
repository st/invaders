(ns st.invaders.pattern)

(def signal-cell \o)

(defn nb-signals-matching
  [radar-grid pattern-grid]
  (let [radar-cells (flatten radar-grid)
        pattern-cells (flatten pattern-grid)]
    (->> (map #(= signal-cell %1 %2) radar-cells pattern-cells)
         (filter true?)
         count)))

(defn nb-signals
  [grid]
  (->> (flatten grid)
       (filter #{signal-cell})
       count))