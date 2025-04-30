(ns st.invaders.grid-test
  (:require
    [clojure.test :refer [deftest is]]
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

