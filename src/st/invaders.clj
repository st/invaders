(ns st.invaders
  (:require
    [clojure.pprint :as pp]
    [st.invaders.detect :as inv]
    [st.invaders.grid :as grid]
    [st.invaders.pattern :as pattern]
    [st.invaders.ui :as ui])
  (:gen-class))

(defn format-detection
  [detection]
  (-> detection
      (update :detection-confidence #(format "%.2f %%" (double %)))))

(defn display-locations
  [{:keys [radar-filename threshold-detection with-transformations] :as args}]

  (pp/print-table [args])

  (let [radar-sample (grid/read-grid radar-filename)

        robot-grid (grid/read-grid "robot.txt")
        jellyfish-grid (grid/read-grid "jellyfish.txt")
        robot-pattern (pattern/make-pattern "robot" robot-grid)
        jellyfish-pattern (pattern/make-pattern "jellyfish" jellyfish-grid)

        invaders-patterns [robot-pattern jellyfish-pattern]

        detections (if with-transformations
                     (inv/detections-with-transformations radar-sample invaders-patterns threshold-detection)
                     (inv/detections radar-sample invaders-patterns threshold-detection))]

    (pp/print-table (mapv format-detection detections))

    (println)
    (ui/display robot-grid)
    (println)
    (ui/display jellyfish-grid)
    (println)
    (ui/display radar-sample)
    (println)
    (ui/display-with-detections radar-sample invaders-patterns detections)
    (println)))

(defn -main
  [& args]
  (display-locations {:radar-filename (first args)
                      :threshold-detection
                      (or (when-let [threshold (second args)] (Double/parseDouble threshold))
                          0.7)}))
