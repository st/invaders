(ns st.invaders.pattern-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [st.invaders.pattern :as p]))

(def hat-pattern [[\- \- \-]
                  [\- \o \-]
                  [\o \- \o]])

(deftest nb-signals-detected-test
  (testing "Simple pattern detection"
    (is (= 3 (p/nb-signals-matching
               [[\- \- \-]
                [\- \o \-]
                [\o \- \o]]
               hat-pattern)))
    (is (= 2 (p/nb-signals-matching
               [[\- \- \-]
                [\- \- \-]
                [\o \- \o]]
               hat-pattern))))
  (testing "Pattern detection with extra signals"
    (is (= 3 (p/nb-signals-matching
               [[\- \o \-]
                [\- \o \-]
                [\o \- \o]]
               hat-pattern))))
  (testing "Pattern detection with noise"
    (is (= 2 (p/nb-signals-matching
               [[\- \- \-]
                [\- \O \-]
                [\o \- \o]]
               hat-pattern)))
    (is (= 3 (p/nb-signals-matching
               [[\- \- \-]
                [\- \o \O]
                [\o \- \o]]
               hat-pattern))))
  )
