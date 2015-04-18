(ns tetris.actions.utils
  (:require
    [clojure.walk :refer :all]
    [clojure.set :refer :all]))

(defn collision-detected? [obj world]
  (not (empty? (intersection obj world))))

(defn line-mask [left-x right-x y]
  (set
    (map #(hash-map :x % :y y) (range left-x (inc right-x)))))

(defn obj-max-y [obj]
  (walk #(:y %) #(apply max %) obj))

(defn obj-min-y [obj]
  (walk #(:y %) #(apply min %) obj))

(defn obj-line-masks [left-x right-x obj]
  (map
    #(line-mask left-x right-x %)
    (range (obj-min-y obj) (inc (obj-max-y obj)))))

(defn bottom-most-empty-y [bottom-y top-y obj]
  (when-let [empty-ys (seq (difference (set (range bottom-y (inc top-y))) (walk :y set obj)))]
    (apply min empty-ys)))
