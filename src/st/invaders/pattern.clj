(ns st.invaders.pattern)

(def signal-cell \o)
(def empty-cell \-)

(defn signal-cell?
  [cell]
  (= signal-cell cell))

(defn empty-cell?
  [cell]
  (= empty-cell cell))

(defn nb-signals-detected
  "Returns the number of signals present in radar-grid and matching pattern-grid.
  When an expected pattern signal is missing returns 0."
  [radar-grid pattern-grid]
  (let [radar-cells (flatten radar-grid)
        pattern-cells (flatten pattern-grid)]

    (loop [[radar-cell & other-radar-cells] radar-cells
           [pattern-cell & other-pattern-cells] pattern-cells
           res 0]

      (if radar-cell
        (cond
          (and (signal-cell? pattern-cell) (empty-cell? radar-cell))
          0

          (and (signal-cell? pattern-cell) (signal-cell? radar-cell))
          (recur other-radar-cells other-pattern-cells (inc res))

          :else
          (recur other-radar-cells other-pattern-cells res))
        res))))

(defn nb-signals
  [grid]
  (->> (flatten grid)
       (filter signal-cell?)
       count))