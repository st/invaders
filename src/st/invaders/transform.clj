(ns st.invaders.transform
  (:require
    [st.invaders.grid :as grid]))

;; Used to create derived patterns (a pattern with grid transformed by geometric functions)

(defn symmetry-vertical-axis
  [grid]
  (let [width (grid/dim-i grid)
        last-index (dec width)]
    (mapv (fn [row]
            (mapv (fn [col]
                    (nth row (- last-index col)))
                  (range width)))
          grid)))

(defn symmetry-horizontal-axis
  [grid]
  (let [height (grid/dim-j grid)
        last-index (dec height)]
    (mapv (fn [line]
            (nth grid (- last-index line)))
          (range height))))

(defn make-transformation
  [name f]
  {:name name
   :fn   f})

(def all-transformations
  [(make-transformation "regular" identity)
   (make-transformation "flip-vertical" symmetry-vertical-axis)
   (make-transformation "flip-horizontal" symmetry-horizontal-axis)
   (make-transformation "flip-both" (comp symmetry-horizontal-axis symmetry-vertical-axis))
   ])

(defn transform-pattern
  [pattern transformation]
  (-> pattern
      (assoc :transformation (:name transformation))
      (update :grid (:fn transformation))))

(defn distinct-on-grid
  "Deduplicates the given patterns on their grids"
  [patterns]
  (loop [[p & other] patterns
         seen-grids #{}
         res []]
    (if p
      (if (seen-grids (:grid p))
        (recur other seen-grids res)
        (recur other (conj seen-grids (:grid p)) (conj res p)))
      res)))

(defn derive-pattern
  [pattern]
  (->> all-transformations
       (map #(transform-pattern pattern %))
       distinct-on-grid))

