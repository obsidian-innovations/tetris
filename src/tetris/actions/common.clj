(ns tetris.actions.common
  (:require
    [clojure.set :refer :all]
    [tetris.actions.utils :refer :all]))

(defn move-to-xy [x y obj]
  (set
    (map #(hash-map :x (+ (:x %) x) :y (+ (:y %) y)) obj)))

(defn move-one-down [obj]
  (move-to-xy 0 -1 obj))

(defn move-one-left [obj]
  (move-to-xy -1 0 obj))

(defn move-one-right [obj]
  (move-to-xy 1 0 obj))

(defn merge-objects [& objs]
  (union objs))

(defn complete-lines [left-x right-x obj]
  (let [lines (group-by :y obj)
        line-length (inc (- right-x left-x))]
    (filter #(= (count %) line-length) (vals lines))))

(defn remove-complete-lines [obj lines]
  (difference obj lines))

(defn collapse-on-y [y obj]
  (let [falling-objs (group-by #(> (:y %) y) obj)]
    (union (move-one-down (set (falling-objs true))) (set (falling-objs false)))))

(defn collapse-bottom-most-empty [bottom-y top-y obj]
  (if-let [min-empty-y (bottom-most-empty-y bottom-y top-y obj)]
    (collapse-on-y min-empty-y obj)
    obj))

(defn collapse-all-empty [bottom-y top-y obj]
  (let [obj-collapsed (collapse-bottom-most-empty bottom-y top-y obj)]
    (if (= obj obj-collapsed) obj-collapsed (recur bottom-y top-y obj-collapsed))))
