(ns st.invaders.core
  (:require
    [st.invaders.grid :as grid]
    [st.invaders.pattern :as p]))

(defn detect-at
  [radar-sample i j threshold-detection pattern]
  (let [pattern-grid (:grid pattern)
        dim-i-pattern (grid/dim-i pattern-grid)
        dim-j-pattern (grid/dim-j pattern-grid)
        radar-clip (grid/clip radar-sample i j dim-i-pattern dim-j-pattern)
        pattern-clip (grid/clip pattern-grid 0 0 (grid/dim-i radar-clip) (grid/dim-j radar-clip))

        nb-signals-pattern (p/nb-signals pattern-grid)
        nb-matching (p/nb-signals-matching radar-clip pattern-clip)
        confidence (/ nb-matching nb-signals-pattern)]

    (when (<= threshold-detection confidence)
      {:location             [i j]
       :id                   (:id pattern)
       :detection-confidence confidence})))

(defn detections
  [radar-sample invaders-patterns threshold-detection]
  (let [dim-j (count radar-sample)
        dim-i (count (first radar-sample))
        all-coordinates (for [i (range dim-i) j (range dim-j)] [i j])]

    (reduce
      (fn [res [i j]]
        (let [detections (->> (map #(detect-at radar-sample i j threshold-detection %) invaders-patterns)
                              (remove nil?))]
          (reduce conj
                  res
                  detections)))
      []
      all-coordinates)))
