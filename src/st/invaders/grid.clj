(ns st.invaders.grid
  (:require
    [clojure.java.io :as io]
    [clojure.string :as s]
    [clojure.term.colors :as color]))

;; A grid is a vector of vectors (same dimension for all)
;; e.g
;; [  [:a :b :c]
;;    [:a :b :c] ]
;;
;; Conventions in namings:
;;
;; i represents the column index
;; j represents the row index

(defn dim-i
  [grid]
  (count (first grid)))

(defn dim-j
  [grid]
  (count grid))

(defn clip
  [grid i j width height]
  (let [bound-i (min (+ i width) (dim-i grid))
        bound-j (min (+ j height) (dim-j grid))]
    (->> (subvec grid j bound-j)
         (mapv (fn [row] (subvec row i bound-i))))))

(defn read-grid
  [filename]
  (->> filename
       io/resource
       slurp
       s/split-lines
       (mapv vec)))

(defn display
  [grid]
  (doseq [row grid]
    (doseq [cell row]
      (print (if (= \o cell)
               (color/on-white " ")
               (color/on-blue " "))))
    (println)))
