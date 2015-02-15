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

(defn merge-objects [obj-a obj-b]
  (union obj-a obj-b) )

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
  (apply difference obj (filter #(= (difference obj %) %) masks)))

(defn remove-complete-lines [left-x right-x obj]
  (apply-line-masks obj (obj-line-masks left-x right-x obj)))

(defn empty-line-ys [bottom-y top-y obj]
  (sort
    (seq
      (intersection
        (set (range bottom-y (inc top-y)))
        (walk :y set obj)))))

(defn collapse-bottom-most-only [bottom-y top-y obj]
  (reduce 
    (fn [empty-y]
      (let [falling (group-by #(> % empty-y))]
        (merge-objects (move-one-down (true falling)) (false falling)))) 
    obj
    (take 1 (empty-line-ys bottom-y top-y obj))))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
