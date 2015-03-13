(ns tetris.core-test
  (:require [clojure.test :refer :all]
            [tetris.tetromino :refer :all]
            [tetris.core :refer :all]))

;(deftest a-test
;  (testing "FIXME, I fail."
;    (is (= 0 1))))

(deftest testing-line-mask
  (is (= #{{:x 0 :y 5} {:x 1 :y 5} {:x 2 :y 5}} (line-mask 0 2 5))))

(deftest moving-objects
  (is (= #{{:x 10 :y 10} {:x 11 :y 10} {:x 10 :y 11} {:x 11 :y 11}} (move-to-xy 10 10 (first o-like)))))

(deftest find-bottom-most-empty-y-coord
  (is
    (zero? (bottom-most-empty-y 0 5 #{{:x 0 :y 1} {:x 1 :y 1} {:x 2 :y 1} {:x 0 :y 4}}))))

(deftest collapse-objects-when-empty-line
  (is
    (empty? (collapse-bottom-most-empty 0 10 #{})))
  (is
    (=
      #{{:x 0 :y 1} {:x 1 :y 1} {:x 2 :y 1} {:x 1 :y 0} {:x 1 :y 3}}
      (collapse-bottom-most-empty 0 10 #{{:x 0 :y 2} {:x 1 :y 2} {:x 2 :y 2} {:x 1 :y 0} {:x 1 :y 4}}))))
