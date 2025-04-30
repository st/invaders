(ns st.invaders.core-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [st.invaders.core :as inv]
    [st.invaders.grid :as grid]))

(def hat-pattern {:id "hat" :grid [[\- \o \-]
                                   [\o \- \o]]})
(def line-pattern {:id "line" :grid [[\o \o \o]]})
(def robot-pattern {:id "robot" :grid (grid/read-grid "robot.txt")})
(def jellyfish-pattern {:id "jellyfish" :grid (grid/read-grid "jellyfish.txt")})

(deftest locations-test
  (testing "detection confidence"
    (is (= {[1 1] [{:location             [1 1]
                    :id                   "hat"
                    :detection-confidence 1}]}
           (inv/locations [[\- \- \- \-]
                           [\- \- \o \-]
                           [\- \o \- \o]]
                          [hat-pattern]
                          0.7)))
    (is (= {[1 1] [{:location             [1 1]
                    :id                   "hat"
                    :detection-confidence 2/3}]}
           (inv/locations [[\- \- \- \-]
                           [\- \- \o \-]
                           [\- \o \- \-]]
                          [hat-pattern]
                          0.6)))
    (is (= {}
           (inv/locations [[\- \- \- \-]
                           [\- \- \o \-]
                           [\- \o \- \-]]
                          [hat-pattern]
                          0.7))))

  (is (= {[1 1] [{:location             [1 1]
                  :id                   "hat"
                  :detection-confidence 1}]
          [1 2] [{:location             [1 2]
                  :id                   "line"
                  :detection-confidence 1}]}
         (inv/locations [[\- \- \- \-]
                         [\- \- \o \-]
                         [\- \o \o \o]]
                        [hat-pattern line-pattern]
                        0.7)))

  (testing "Given example"
    (is (= {[16 28] [{:location [16 28], :id "jellyfish", :detection-confidence 11/12}],
            [35 15] [{:location [35 15], :id "jellyfish", :detection-confidence 8/9}],
            [42 0]  [{:location [42 0], :id "jellyfish", :detection-confidence 31/36}],
            [60 13] [{:location [60 13], :id "robot", :detection-confidence 19/23}],
            [74 1]  [{:location [74 1], :id "robot", :detection-confidence 41/46}],
            [77 2]  [{:location [77 2], :id "jellyfish", :detection-confidence 13/18}],
            [82 41] [{:location [82 41], :id "jellyfish", :detection-confidence 8/9}],
            [85 12] [{:location [85 12], :id "robot", :detection-confidence 20/23}]}
           (inv/locations (grid/read-grid "radar-sample.txt")
                          [robot-pattern jellyfish-pattern]
                          0.7))))
  )

(deftest locations-overlap-test
  (is (= {[0 1] [{:detection-confidence 2/3
                  :id                   "line"
                  :location             [0 1]}]
          [1 1] [{:detection-confidence 1
                  :id                   "hat"
                  :location             [1 1]}
                 {:detection-confidence 2/3
                  :id                   "line"
                  :location             [1 1]}]
          [1 2] [{:detection-confidence 2/3
                  :id                   "line"
                  :location             [1
                                         2]}]}
         (inv/locations [[\- \- \- \-]
                         [\- \o \o \-]
                         [\- \o \- \o]]
                        [hat-pattern line-pattern]
                        0.6))))