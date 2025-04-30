(ns st.invaders.core-test
  (:require
    [clojure.test :refer [deftest is]]
    [st.invaders.core :as inv]))

(def hat-pattern {:id "hat" :grid [[\- \- \-]
                                   [\- \o \-]
                                   [\o \- \o]]})

(def line-pattern {:id "line" :grid [[\o \o \o]]})

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
  )
