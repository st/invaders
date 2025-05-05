(ns st.invaders.ui
  (:require
    [clojure.term.colors :as color]
    [st.invaders.grid :as grid]))

;; Basic ASCII display with some coloring.

(defn display
  [grid]
  (doseq [row grid]
    (doseq [cell row]
      (print (if (= \o cell)
               (color/on-white " ")
               (color/on-blue " "))))
    (println)))

(defn change-signal
  [pattern]
  (update pattern :grid #(grid/replace-value % \o \+)))

(defn display-with-detections
  [grid invaders-patterns invaders-detections]
  (let [invaders-patterns (mapv change-signal invaders-patterns)
        invaders-patterns-map (reduce
                                (fn [m p] (assoc m (:id p) p))
                                {}
                                invaders-patterns)
        grid-overlapped (reduce
                          (fn [grid detection]
                            (grid/replace-at grid
                                             (first (:location detection))
                                             (second (:location detection))
                                             (:grid (get invaders-patterns-map (:id detection)))))
                          grid
                          invaders-detections)]
    (doseq [row grid-overlapped]
      (doseq [cell row]
        (print (condp = cell
                 \o (color/on-white " ")
                 \+ (color/on-red " ")
                 (color/on-blue " "))))
      (println))))