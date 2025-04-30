(ns st.invaders.grid
  (:require
    [clojure.java.io :as io]
    [clojure.string :as s]))

(defn dim-i
  [grid]
  (count (first grid)))

(defn dim-j
  [grid]
  (count grid))

(defn clip
  [grid i j clip-height clip-width]
  (let [bound-i (min (+ i clip-height) (dim-i grid))
        bound-j (min (+ j clip-width) (dim-j grid))]
    (->> (subvec grid j bound-j)
         (mapv (fn [row] (subvec row i bound-i))))))

(defn read-grid
  [filename]
  (->> filename
       io/resource
       slurp
       s/split-lines
       (mapv vec)))
