(ns st.invaders.pattern)

;; A pattern is a named grid with simple matching functions

(def signal-cell \o)

(def mem-flatten (memoize flatten))

(defn nb-signals-matching
  "Computes the number of cells containing a signal at same location for both grids."
  [radar-grid pattern-grid]
  (let [radar-cells (flatten radar-grid)
        pattern-cells (mem-flatten pattern-grid)]
    (->> (mapv #(= signal-cell %1 %2) radar-cells pattern-cells)
         (filter true?)
         count)))

(defn nb-signals
  "Returns the number of signals for a grid.
  e.g
  [[o _ o]] contains 2 signals.
  "
  [grid]
  (->> (flatten grid)
       (filter #{signal-cell})
       count))

(def mem-nb-signals
  (memoize nb-signals))

(defn make-pattern
  [id grid]
  {:id id :grid grid})
