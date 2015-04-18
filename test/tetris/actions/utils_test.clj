(ns tetris.actions.utils-test
  (:require
    [clojure.test :refer :all]
    [tetris.actions.utils :refer :all]))

(deftest line-mask-test
  (is (= #{{:x 0 :y 5} {:x 1 :y 5} {:x 2 :y 5}} (line-mask 0 2 5))))

(deftest bottom-most-empty-y-test
  (is
    (zero? (bottom-most-empty-y 0 5 #{{:x 0 :y 1} {:x 1 :y 1} {:x 2 :y 1} {:x 0 :y 4}}))))
