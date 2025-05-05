(ns st.invaders.detect
  (:require
    [st.invaders.grid :as grid]
    [st.invaders.pattern :as p]
    [st.invaders.transform :as t]))

(defn detect-at
  "Returns a detection when the coordinate [i j] is the top-left corner of a clip matching the pattern.

  `threshold-detection` is a float value.
  e.g. For `threshold-detection` set to 0.6 and a pattern containing 10 signals,
  the clips at a given coordinate must match on at least 6 signal cells.
  "
  [radar-sample i j threshold-detection pattern]
  (let [pattern-grid (:grid pattern)
        dim-i-pattern (grid/dim-i pattern-grid)
        dim-j-pattern (grid/dim-j pattern-grid)
        radar-clip (grid/clip radar-sample i j dim-i-pattern dim-j-pattern)
        pattern-clip (grid/clip pattern-grid 0 0 (grid/dim-i radar-clip) (grid/dim-j radar-clip))

        nb-signals-pattern (p/mem-nb-signals pattern-grid)

        nb-matching (p/nb-signals-matching radar-clip pattern-clip)

        confidence (/ nb-matching nb-signals-pattern)]

    (when (<= threshold-detection confidence)
      (let [detection {:location             [i j]
                       :id                   (:id pattern)
                       :detection-confidence confidence}]
        (if (:transform pattern)
          (assoc detection :transformation (:transform pattern))
          detection)))))

(defn detections
  "Returns a lazy sequence of detections in the form
  (
  ...
  {:location [16 28], :id \"jellyfish\" :detection-confidence 11/12}
  {:location [35 15], :id \"jellyfish\", :detection-confidence 8/9}
  {:location [42 0], :id \"jellyfish\", :detection-confidence 31/36}
  ...
  )"
  [radar-sample invaders-patterns threshold-detection]
  (let [dim-j (grid/dim-j radar-sample)
        dim-i (grid/dim-i radar-sample)
        all-coordinates (for [i (range dim-i) j (range dim-j)] [i j])]

    (->> all-coordinates
         (mapcat (fn [[i j]] (->> invaders-patterns
                                  (pmap (fn [pattern] (detect-at radar-sample i j threshold-detection pattern)))
                                  (remove nil?))))
         (remove empty?))))

(defn detections-with-transformations
  [radar-sample invaders-patterns threshold-detection]
  (detections radar-sample
              (mapcat t/derive-pattern invaders-patterns)
              threshold-detection))
