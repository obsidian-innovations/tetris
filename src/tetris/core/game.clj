(ns tetris.core.game
  (:require
    [tetris.actions.game :as ag :refer [generate-events-chain]]
    [tetris.core.config :as config]
    [tetris.core.tetrominos :as ts]))

(defn- init-tetromino [sprites]
  {:coords {:x (/ (:board-width config/main) 2) :y (:board-heigh config/main)}
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
  (let [all (repeatedly #(init-tetromino (rand-nth ts/all)))]
    {:current (first all)
     :next (rest all)}))

(defn- init-events []
  (ag/generate-events-chain (:gravity-event-frequency config/main)))

(defn- init-walls []
  (init-wall-bricks
    {:bottom-y 0
     :top-y (:board-heigh config/main)
     :left-x 0
     :right-x (inc (:board-width config/main))
     :wall-bricks #{}}))

(defn- init-stats []
  {:completed-lines-count 0})

(defn init-state []
  {:walls (init-walls)
   :tetrominos (init-tetrominos)
   :heap #{}
   :stats (init-stats)
   :events (init-events)})
