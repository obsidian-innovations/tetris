(ns tetris.core.game-test
  (:require [clojure.test :refer :all]))

;(deftest init-tetromino
;  (let [t (#'state/init-tetromino ts/i-like)]
;    (testing "tetromino should have start coords"
;      (is (number? (get-in t [:coords :x])))
;      (is (number? (get-in t [:coords :y]))))
;
;    (testing "tetromino should contain tetromino sprites"
;      (is (vector? (:sprites t)))
;      (is (= (count (:sprites t)) 4))
;      (is (every? set? (:sprites t))))
;
;    (testing "tetromino should contain infinite stream of sprites"
;      (is (not (realized? (:positions t))))
;      (is
;        (=
;          (first (:positions t))
;          (first (drop (count (:sprites t)) (:positions t))))))))


;(deftest init-wall-bricks
;  (let [w- {:bottom-y 0
;            :top-y 2
;            :left-x 0
;            :right-x 2
;            :wall-bricks #{}}
;        w (#'state/init-wall-bricks w-)]
;
;    (testing "should create wall boundary bricks"
;      (is
;        (=
;            #{{:x 0 :y 0} {:x 1 :y 0} {:x 2 :y 0} {:x 0 :y 1} {:x 2 :y 1} {:x 0 :y 2} {:x 2 :y 2}}
;          (:wall-bricks w))))))
