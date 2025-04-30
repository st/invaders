(ns st.invaders.core-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [st.invaders.pattern :as p]))

(def beast-hat-pattern [[\- \- \-]
                        [\- \o \-]
                        [\o \- \o]])

(deftest detect-pattern?-test
  (testing "Simple pattern detection"
    (is (p/detect-pattern?
          [[\- \- \-]
           [\- \o \-]
           [\o \- \o]]
          beast-hat-pattern))
    (is (not (p/detect-pattern?
               [[\- \- \-]
                [\- \- \-]
                [\o \- \o]]
               beast-hat-pattern))))
  (testing "Pattern detection with extra signals"
    (is (p/detect-pattern?
          [[\- \o \-]
           [\- \o \-]
           [\o \- \o]]
          beast-hat-pattern)))
  (testing "Pattern detection with noise"
    (is (p/detect-pattern?
          [[\- \- \-]
           [\- \O \-]
           [\o \- \o]]
          beast-hat-pattern))
    (is (p/detect-pattern?
          [[\- \- \-]
           [\- \o \O]
           [\o \- \o]]
          beast-hat-pattern)))
  )
