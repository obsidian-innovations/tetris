(ns tetris.actions.common-test
  (:require
    [clojure.test :refer :all]
    [tetris.core.tetrominos :refer [o-like]]
    [tetris.actions.common :refer :all]))

(deftest move-to-xy-test
  (is
    (=
        #{{:x 10 :y 10} {:x 11 :y 10} {:x 10 :y 11} {:x 11 :y 11}}
      (move-to-xy 10 10 (first o-like)))))


(deftest collapse-bottom-most-empty-test

  (is
    (empty? (collapse-bottom-most-empty 0 10 #{})))

  (is
    (=
      #{{:x 0 :y 1} {:x 1 :y 1} {:x 2 :y 1} {:x 1 :y 0} {:x 1 :y 3}}
      (collapse-bottom-most-empty 0 10 #{{:x 0 :y 2} {:x 1 :y 2} {:x 2 :y 2} {:x 1 :y 0} {:x 1 :y 4}}))))


(deftest collapse-all-empty-test
  (is
    (=
      #{{:x 0 :y 1} {:x 1 :y 1} {:x 2 :y 1} {:x 1 :y 0} {:x 1 :y 2}}
      (collapse-all-empty 0 10 #{{:x 0 :y 2} {:x 1 :y 2} {:x 2 :y 2} {:x 1 :y 0} {:x 1 :y 4}}))))
