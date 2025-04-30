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
        invaders-patterns (mapv #(pattern/make-pattern % (grid/read-grid (str % ".txt"))) ["jellyfish" "robot"])
        locations (inv/locations radar-sample invaders-patterns threshold-detection)]

    (pp/print-table (mapv format-location locations))))

(defn -main
  [& args]
  (display-locations {:radar-filename (first args)
                      :threshold-detection
                      (or (when-let [threshold (second args)] (Double/parseDouble threshold))
                          0.7)}))
