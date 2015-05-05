(ns tetris.actions.utils
  (:require
    [clojure.walk :refer :all]
    [clojure.set :refer :all]))

(defn collision-detected? [obj world]
  (not (empty? (intersection obj world))))

(defn obj-max-y [obj]
  (walk #(:y %) #(apply max %) obj))

(defn obj-min-y [obj]
  (walk #(:y %) #(apply min %) obj))

(defn bottom-most-empty-y [bottom-y top-y obj]
  (when-let [empty-ys (seq (difference (set (range bottom-y (inc top-y))) (walk :y set obj)))]
    (apply min empty-ys)))
