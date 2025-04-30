(ns st.invaders.pattern-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [st.invaders.pattern :as p]))

(def hat-pattern [[\- \- \-]
                  [\- \o \-]
                  [\o \- \o]])

(deftest nb-signals-detected-test
  (testing "Simple pattern detection"
    (is (= 3 (p/nb-signals-detected
               [[\- \- \-]
                [\- \o \-]
                [\o \- \o]]
               hat-pattern)))
    (is (= 0 (p/nb-signals-detected
               [[\- \- \-]
                [\- \- \-]
                [\o \- \o]]
               hat-pattern))))
  (testing "Pattern detection with extra signals"
    (is (= 3 (p/nb-signals-detected
               [[\- \o \-]
                [\- \o \-]
                [\o \- \o]]
               hat-pattern))))
  (testing "Pattern detection with noise"
    (is (= 2 (p/nb-signals-detected
               [[\- \- \-]
                [\- \O \-]
                [\o \- \o]]
               hat-pattern)))
    (is (= 3 (p/nb-signals-detected
               [[\- \- \-]
                [\- \o \O]
                [\o \- \o]]
               hat-pattern))))
  )
