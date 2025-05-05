(ns st.invaders
  (:require
    [clojure.java.io :as io]
    [clojure.pprint :as pp]
    [clojure.string :as s]
    [st.invaders.detect :as inv]
    [st.invaders.grid :as grid]
    [st.invaders.pattern :as pattern]
    [st.invaders.ui :as ui]
    [clojure.tools.cli :as cli])
  (:gen-class))

(defn format-detection
  [detection]
  (update detection :detection-confidence #(format "%.2f %%" (double %))))

(defn display-locations
  [{:keys [radar-sample threshold extra-transform] :as options}]

  (let [radar-sample (grid/parse-file radar-sample)
        robot-grid (grid/parse-resource "robot.txt")
        jellyfish-grid (grid/parse-resource "jellyfish.txt")

        robot-pattern (pattern/make-pattern "robot" robot-grid)
        jellyfish-pattern (pattern/make-pattern "jellyfish" jellyfish-grid)

        invaders-patterns [robot-pattern jellyfish-pattern]

        detections (if extra-transform
                     (inv/detections-with-transformations radar-sample invaders-patterns threshold)
                     (inv/detections radar-sample invaders-patterns threshold))]

    (println "PARAMETERS")
    (pp/print-table [options])

    (println "DETECTIONS")
    (pp/print-table (mapv format-detection detections))

    (println "INVADERS")
    (ui/display robot-grid)
    (println)
    (ui/display jellyfish-grid)
    (println "RADAR-SAMPLE")
    (ui/display radar-sample)
    (println "RADAR-SAMPLE WITH DETECTIONS")
    (ui/display-with-detections radar-sample invaders-patterns detections)
    (println)
    (System/exit 0)))

(def cli-options
  [["-r" "--radar-sample RADAR-SAMPLE" "Radar sample filename (mandatory)"
    :validate [(fn [filename]
                 (when filename
                   (.exists (io/file filename))))
               "Must be a valid filename"]
    :missing "Please provide a radar sample with -r RADAR-SAMPLE"]

   ["-t" "--threshold THRESHOLD" "Threshold detection"
    :default 0.8
    :parse-fn #(Double/parseDouble %)
    :validate [#(<= 0.5 % 1.0) "The threshold detection be a float value between 0.5 and 1.0"]]

   ["-x" "--extra-transform" "Include transformations" :default false]

   ["-h" "--help"]
   ])

(defn usage
  [options-summary]
  (->> ["Let's detect those pesky invaders !"
        ""
        "Usage: clojure -M:run-m [options]"
        ""
        "Options:"
        options-summary
        ""
        "Example:"
        ""
        "clojure -M:run-m -r <filename> -t 0.9"
        ""]
       (s/join \newline)))

(defn error-msg
  [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (s/join \newline errors)))

(defn validate-args
  [args]
  (let [{:keys [options errors summary]} (cli/parse-opts args cli-options)]
    (cond
      (:help options) {:exit-message (usage summary) :ok? true}
      errors {:exit-message (error-msg errors)}
      :else {:options options})))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn process-command-args
  [args]
  (let [{:keys [options exit-message ok?]} args]
    (if exit-message
      (exit (if ok? 0 1) exit-message)

      (display-locations options))))

(defn -main
  [& args]
  (->> args
       validate-args
       process-command-args))

