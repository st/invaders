(ns st.invaders
  (:require
    [clojure.pprint :as pp]
    [st.invaders.core :as inv]
    [st.invaders.grid :as grid]
    [st.invaders.pattern :as pattern])
  (:gen-class))

(defn format-location
  [location]
  (-> location
      (update :detection-confidence #(format "%.2f %%" (double %)))))

(defn display-locations
  [{:keys [radar-filename threshold-detection]}]

  (println "Radar sample        : " radar-filename)
  (println "Threshold detection : " threshold-detection)

  (let [radar-sample (grid/read-grid radar-filename)
        robot-grid (grid/read-grid "robot.txt")
        jellyfish-grid (grid/read-grid "jellyfish.txt")
        robot-pattern (pattern/make-pattern "robot" robot-grid)
        jellyfish-pattern (pattern/make-pattern "jellyfish" jellyfish-grid)
        invaders-patterns [robot-pattern jellyfish-pattern]
        locations (inv/locations radar-sample invaders-patterns threshold-detection)]

    (grid/display robot-grid)
    (println)
    (grid/display jellyfish-grid)
    (println)
    (grid/display radar-sample)
    (println)
    (pp/print-table (mapv format-location locations))))

(defn -main
  [& args]
  (display-locations {:radar-filename (first args)
                      :threshold-detection
                      (or (when-let [threshold (second args)] (Double/parseDouble threshold))
                          0.7)}))
