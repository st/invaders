(ns st.invaders.core
  (:require
    [st.invaders.grid :as grid]
    [st.invaders.pattern :as p]))

(def ratio-signals-for-detection 0.7)

(defn detect-at
  [radar-sample i j pattern]
  (let [pattern-grid (:grid pattern)
        min-nb-signals-detected (* ratio-signals-for-detection (p/nb-signals pattern-grid))

        dim-i-pattern (grid/dim-i pattern-grid)
        dim-j-pattern (grid/dim-j pattern-grid)

        radar-clip (grid/clip radar-sample i j dim-i-pattern dim-j-pattern)
        pattern-clip (grid/clip pattern-grid 0 0 (grid/dim-i radar-clip) (grid/dim-j radar-clip))]

    (when (<= min-nb-signals-detected (p/nb-signals-matching radar-clip pattern-clip))
      {:location [i j]
       :id       (:id pattern)})))

(defn locations
  [radar-sample invaders-patterns]
  (let [dim-j (count radar-sample)
        dim-i (count (first radar-sample))
        all-coordinates (for [i (range dim-i) j (range dim-j)] [i j])]

    (reduce
      (fn [res [i j]]
        (let [detections (->> (map #(detect-at radar-sample i j %) invaders-patterns)
                              (remove nil?))]
          (reduce (fn [acc detection]
                    (update acc (:location detection) (fnil conj []) (:id detection)))
                  res
                  detections)
          ))
      {}
      all-coordinates)))
