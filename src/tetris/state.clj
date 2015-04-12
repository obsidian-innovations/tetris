(ns tetris.state
  (:require [tetris.tetromino :as t]))


(defn- init-tetromino [sprites]
  {:coords {:x 10 :y 15} 
   :sprites sprites
   :positions (cycle sprites)})

(defn- init-wall-bricks [b]
  (->>
    (for [x (range (b :left-x) (inc (b :right-x)))
          y (range (b :bottom-y) (inc (b :top-y)))]
      {:x x :y y})
    (filter #(or (= (% :x) (b :left-x)) (= (% :x) (b :right-x)) (= (% :y) (b :bottom-y))))
    (set)
    (assoc b :wall-bricks)))

(defn- init-tetrominos []
  (let [all (repeatedly #(init-tetromino (rand-nth tetris.tetromino/all)))]
    {:current (first all)
     :next (rest all)}))

(defn- init-events []
  (let [user-action (repeat 30 :user-action)
        gravity-action [:gravity-action]
        init-codes (flatten (interleave gravity-action (vector user-action)))]
    (cycle init-codes)))

(defn- init-walls []
  (init-wall-bricks
    {:bottom-y 0
     :top-y 15
     :left-x 0
     :right-x 20
     :wall-bricks #{}}))

(defn init-state []
  {:walls (init-walls)
   :tetrominos (init-tetrominos)
   :heap #{}
   :events (init-events)})
