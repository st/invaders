(ns st.invaders.detect-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [st.invaders.detect :as inv]
    [st.invaders.grid :as grid]
    [st.invaders.pattern :as pattern]))

(def hat-pattern (pattern/make-pattern "hat" [[\- \o \-]
                                              [\o \- \o]]))
(def line-pattern (pattern/make-pattern "line" [[\o \o \o]]))
(def robot-pattern (pattern/make-pattern "robot" (grid/parse-resource "robot.txt")))
(def jellyfish-pattern (pattern/make-pattern "jellyfish" (grid/parse-resource "jellyfish.txt")))

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
           (inv/detections (grid/parse-resource "radar-sample.txt")
                           [robot-pattern jellyfish-pattern]
                           0.7))))
  )

(deftest detections-overlap-test
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

(deftest detections-with-transformations-test
  (is (= [{:detection-confidence 2/3
           :id                   "hat"
           :location             [0 1]
           :transformation       "flip-horizontal"}
          {:detection-confidence 2/3
           :id                   "line"
           :location             [0 1]
           :transformation       "regular"}
          {:detection-confidence 1
           :id                   "hat"
           :location             [1 1]
           :transformation       "regular"}
          {:detection-confidence 2/3
           :id                   "line"
           :location             [1 1]
           :transformation       "regular"}
          {:detection-confidence 2/3
           :id                   "hat"
           :location             [1 2]
           :transformation       "flip-horizontal"}
          {:detection-confidence 2/3
           :id                   "line"
           :location             [1 2]
           :transformation       "regular"}
          {:detection-confidence 2/3
           :id                   "hat"
           :location             [2 1]
           :transformation       "flip-horizontal"}]
         (inv/detections-with-transformations [[\- \- \- \-]
                                               [\- \o \o \-]
                                               [\- \o \- \o]]
                                              [hat-pattern line-pattern]
                                              0.6)))
  )

(comment

  ;; Initial version
  ;; Execution time mean : 957 ms

  (cri/quick-bench
    (doall (inv/detections (grid/parse-resource "radar-sample.txt")
                           [robot-pattern jellyfish-pattern]
                           0.7)))
  ;; Evaluation count : 6 in 6 samples of 1 calls.
  ;; Execution time mean : 130.633113 ms
  ;; Execution time std-deviation : 18.925389 ms
  ;; Execution time lower quantile : 111.305388 ms ( 2.5%)
  ;; Execution time upper quantile : 153.666419 ms (97.5%)
  ;; Overhead used : 6.584589 ns

  )

