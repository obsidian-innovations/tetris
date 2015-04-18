(ns tetris.actions.tetromino
  (:require
    [clojure.set :refer :all]
    [tetris.actions.utils :refer :all]
    [tetris.actions.common :refer :all]))

(defn move-down [state]
  (update-in state [:tetrominos :current :coords :y] dec))

(defn move-right [state]
  (update-in state [:tetrominos :current :coords :x] inc))

(defn move-left [state]
  (update-in state [:tetrominos :current :coords :x] dec))

(defn rotate-left [state]
  (update-in state [:tetrominos :current :positions] #(drop (dec (count (get-in state [:tetrominos :current :sprites]))) %)))

(defn rotate-right [state]
  (update-in state [:tetrominos :current :positions] rest))

(defn next-tetromino [state]
  (update-in state [:tetrominos] #(hash-map :current (first (:next %)) :next (rest (:next %)))))

(defn move-to-coords [state]
  (move-to-xy
    (:x (:coords (:current (:tetrominos state))))
    (:y (:coords (:current (:tetrominos state))))
    (first (:positions (:current (:tetrominos state))))))

(defn move-when-no-collision [state action]
  (let [next-state (action state)]
    (if (collision-detected?
          (move-to-coords next-state)
          (union (:heap next-state) (:wall-bricks (:walls next-state))))
      state
      next-state)))
