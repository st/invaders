(ns st.invaders.core-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [st.invaders.core :as inv]
    [st.invaders.grid :as grid]
    [st.invaders.pattern :as pattern]))

(def hat-pattern (pattern/make-pattern "hat" [[\- \o \-]
                                              [\o \- \o]]))
(def line-pattern (pattern/make-pattern "line" [[\o \o \o]]))
(def robot-pattern (pattern/make-pattern "robot" (grid/read-grid "robot.txt")))
(def jellyfish-pattern (pattern/make-pattern "jellyfish" (grid/read-grid "jellyfish.txt")))

(deftest detections-test
  (testing "detection confidence"
    (is (= [{:location             [1 1]
             :id                   "hat"
             :detection-confidence 1}]
           (inv/detections [[\- \- \- \-]
                           [\- \- \o \-]
                           [\- \o \- \o]]
                           [hat-pattern]
                           0.7)))
    (is (= [{:location             [1 1]
             :id                   "hat"
             :detection-confidence 2/3}]
           (inv/detections [[\- \- \- \-]
                           [\- \- \o \-]
                           [\- \o \- \-]]
                           [hat-pattern]
                           0.6)))
    (is (= []
           (inv/detections [[\- \- \- \-]
                           [\- \- \o \-]
                           [\- \o \- \-]]
                           [hat-pattern]
                           0.7))))

  (is (= [{:location             [1 1]
           :id                   "hat"
           :detection-confidence 1}
          {:location             [1 2]
           :id                   "line"
           :detection-confidence 1}]
         (inv/detections [[\- \- \- \-]
                         [\- \- \o \-]
                         [\- \o \o \o]]
                         [hat-pattern line-pattern]
                         0.7)))

  (testing "Given example"
    (is (= [{:location [16 28], :id "jellyfish", :detection-confidence 11/12}
            {:location [35 15], :id "jellyfish", :detection-confidence 8/9}
            {:location [42 0], :id "jellyfish", :detection-confidence 31/36}
            {:location [60 13], :id "robot", :detection-confidence 19/23}
            {:location [74 1], :id "robot", :detection-confidence 41/46}
            {:location [77 2], :id "jellyfish", :detection-confidence 13/18}
            {:location [82 41], :id "jellyfish", :detection-confidence 8/9}
            {:location [85 12], :id "robot", :detection-confidence 20/23}]
           (inv/detections (grid/read-grid "radar-sample.txt")
                           [robot-pattern jellyfish-pattern]
                           0.7))))
  )

(deftest locations-overlap-test
  (is (= [{:detection-confidence 2/3
           :id                   "line"
           :location             [0 1]}
          {:detection-confidence 1
           :id                   "hat"
           :location             [1 1]}
          {:detection-confidence 2/3
           :id                   "line"
           :location             [1 1]}
          {:detection-confidence 2/3
           :id                   "line"
           :location             [1 2]}]
         (inv/detections [[\- \- \- \-]
                         [\- \o \o \-]
                         [\- \o \- \o]]
                         [hat-pattern line-pattern]
                         0.6))))