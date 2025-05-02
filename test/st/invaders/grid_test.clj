(ns st.invaders.grid-test
  (:require
    [clojure.test :refer [deftest is]]
    [st.invaders.grid :as grid]
    [st.invaders.grid :refer [clip]]))

(deftest clip-test
  (let [grid [[:a :b :c]
              [:d :e :f]]]
    (is (= [[:a]] (clip grid 0 0 1 1)))
    (is (= [[:a :b :c]] (clip grid 0 0 3 1)))
    (is (= [[:b :c]] (clip grid 1 0 2 1)))
    (is (= [[:b :c]] (clip grid 1 0 10 1)))

    (is (= [[:a] [:d]] (clip grid 0 0 1 2)))
    (is (= [[:a] [:d]] (clip grid 0 0 1 10)))
    ))

(deftest replace-at-test
  (let [grid [[:a :b :c]
              [:d :e :f]
              [:g :h :i]]
        sub [[:x :y]
             [:z :t]]]

    (is (= [[:a :b :c]
            [:d :e :f]
            [:g :x :y]]
           (grid/replace-at grid 1 2 sub)))

    (is (= [[:x :y :c]
            [:z :t :f]
            [:g :h :i]]
           (grid/replace-at grid 0 0 sub)))

    (is (= [[:a :b :c]
            [:d :e :f]
            [:g :h :x]]
           (grid/replace-at grid 2 2 sub)))
    ))

(deftest replace-value-test
  (is (= [[:X :b :c]
          [:d :X :X]
          [:g :h :i]]
         (grid/replace-value
           [[:a :b :c]
            [:d :a :a]
            [:g :h :i]]
           :a
           :X
           ))))