(ns tetris.core
  (:require
    [clojure.set :refer :all]
    [clojure.walk :refer :all])
  (:gen-class))


(defn move-to-xy [x y obj]
  (set
    (map #(hash-map :x (+ (:x %) x) :y (+ (:y %) y)) obj)))

(defn move-one-down [obj]
  (move-to-xy 0 -1 obj))

(defn move-one-left [obj]
  (move-to-xy -1 0 obj))

(defn move-one-right [obj]
  (move-to-xy 1 0 obj))

(defn collision-detected? [obj world]
  (not (empty? (intersection obj world))))

(defn merge-objects [& objs]
  (union objs))

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

(defn apply-line-masks [obj masks]
  (apply difference obj (filter #(= (intersection obj %) %) masks)))

(defn remove-complete-lines [left-x right-x obj]
  (apply-line-masks obj (obj-line-masks left-x right-x obj)))

(defn bottom-most-empty-y [bottom-y top-y obj]
  (apply min (difference (set (range bottom-y (inc top-y))) (walk :y set obj))))

(defn collapse-on-y [y obj]
  (let [falling-objs (group-by #(> (:y %) y) obj)]
    (union (move-one-down (set (falling-objs true))) (set (falling-objs false)))))

(defn collapse-bottom-most-empty [bottom-y top-y obj]
  (if (empty? obj)
    obj
    (collapse-on-y (bottom-most-empty-y bottom-y top-y obj) obj)))

(defn collapse-all-empty [bottom-y top-y obj]
  (let [obj-collapsed (collapse-bottom-most-empty bottom-y top-y obj)]
    (if (= obj obj-collapsed) obj-collapsed (recur bottom-y top-y obj-collapsed))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
