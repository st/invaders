(ns st.invaders.grid
  (:require
    [clojure.java.io :as io]
    [clojure.string :as s]))

;; A grid is a vector of vectors (same dimension for all)
;; e.g
;; [  [:a :b :c]
;;    [:a :b :c] ]
;;
;; Conventions:
;;
;; i represents the column index
;; j represents the row index
;;

(defn read-grid
  [filename]
  (->> filename
       io/resource
       slurp
       s/split-lines
       (mapv vec)))

(defn dim-i
  [grid]
  (count (first grid)))

(defn dim-j
  [grid]
  (count grid))

(defn clip
  "Returns a subgrid with top-left corner at [i j] and dimensions `width` * `height` inside `grid`.
  If the result subgrid extends beyond `grid`, it is truncated."
  [grid i j width height]
  (let [bound-i (min (+ i width) (dim-i grid))
        bound-j (min (+ j height) (dim-j grid))]
    (->> (subvec grid j bound-j)
         (mapv (fn [row] (subvec row i bound-i))))))

(defn replace-at
  [grid i j subgrid]
  (let [dim-sub-grid-i (min (dim-i subgrid) (- (dim-i grid) i))
        dim-sub-grid-j (min (dim-j subgrid) (- (dim-j grid) j))
        before (subvec grid 0 j)
        middle (mapv (fn [row replace]
                       (-> (subvec row 0 i)
                           (into (subvec replace 0 dim-sub-grid-i))
                           (into (subvec row (+ i dim-sub-grid-i)))))
                     (subvec grid j (+ j dim-sub-grid-j))
                     subgrid)
        after (subvec grid (+ j dim-sub-grid-j))]
    (-> before
        (into middle)
        (into after))))

(defn replace-value
  [grid old new]
  (mapv
    (fn [row]
      (mapv (fn [cell]
              (if (= cell old) new cell)) row))
    grid))