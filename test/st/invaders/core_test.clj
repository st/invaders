(ns st.invaders.core-test
  (:require
    [clojure.test :refer [deftest is]]
    [st.invaders.core :as inv]
    [st.invaders.grid :as grid]))

(def hat-pattern {:id "hat" :grid [[\- \- \-]
                                   [\- \o \-]
                                   [\o \- \o]]})

(def line-pattern {:id "line" :grid [[\o \o \o]]})

(def robot-pattern {:id "robot" :grid (grid/read-grid "robot.txt")})

(def jellyfish-pattern {:id "jellyfish" :grid (grid/read-grid "jellyfish.txt")})

(deftest locations-test
  (is (= {[1 0] ["hat"]}
         (inv/locations [[\- \- \- \-]
                         [\- \- \o \-]
                         [\- \o \- \o]]
                        [hat-pattern])))

  (is (= {[1 0] ["hat"]
          [1 2] ["line"]}
         (inv/locations [[\- \- \- \-]
                         [\- \- \o \-]
                         [\- \o \o \o]]
                        [hat-pattern line-pattern])))
  (is (= {[16 28] ["jellyfish"]
          [35 15] ["jellyfish"]
          [42 0]  ["jellyfish"]
          [60 13] ["robot"]
          [74 1]  ["robot"]
          [77 2]  ["jellyfish"]
          [82 41] ["jellyfish"]
          [85 12] ["robot"]}
         (inv/locations (grid/read-grid "radar-sample.txt")
                        [robot-pattern jellyfish-pattern])))

  )
