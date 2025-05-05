(ns st.invaders.transform-test
  (:require
    [clojure.test :refer [deftest is testing]]
    [st.invaders.transform :as t]
    [st.invaders.pattern :as p]
    ))

(deftest symmetry-vertical-axis-test
  (is (= [[:c :b :a]
          [:f :e :d]
          [:i :h :g]]
         (t/symmetry-vertical-axis
           [[:a :b :c]
            [:d :e :f]
            [:g :h :i]])))
  (is (= [[:b :a]
          [:d :c]]
         (t/symmetry-vertical-axis
           [[:a :b]
            [:c :d]])))
  )

(deftest symmetry-horizontal-axis-test
  (is (= [[:g :h :i]
          [:d :e :f]
          [:a :b :c]]
         (t/symmetry-horizontal-axis
           [[:a :b :c]
            [:d :e :f]
            [:g :h :i]])))
  (is (= [[:c :d]
          [:a :b]]
         (t/symmetry-horizontal-axis
           [[:a :b]
            [:c :d]])))
  )

(deftest derive-pattern-test
  (is (= [{:grid      [[\_ \o]
                       [\o \o]]
           :id        "p1"
           :transform "regular"}
          {:grid      [[\o \_]
                       [\o \o]]
           :id        "p1"
           :transform "flip-vertical"}
          {:grid      [[\o \o]
                       [\_ \o]]
           :id        "p1"
           :transform "flip-horizontal"}
          {:grid      [[\o \o]
                       [\o \_]]
           :id        "p1"
           :transform "flip-both"}]
         (t/derive-pattern (p/make-pattern "p1"
                                           [[\_ \o]
                                            [\o \o]]))))
  (testing "Avoid duplicate grids"
    (is (= [{:grid      [[\_ \_]
                         [\o \o]]
             :id        "p1"
             :transform "regular"}
            {:grid      [[\o \o]
                         [\_ \_]]
             :id        "p1"
             :transform "flip-horizontal"}]
           (t/derive-pattern (p/make-pattern "p1"
                                             [[\_ \_]
                                              [\o \o]])))))
  )
