(ns tetris.board
  (:require [tetris.tetromino :as t]))

(def boundaries {:bottom-y 0
                 :top-y 15
                 :left-x 0
                 :right-x 20
                 :wall-bricks #{}})

(defn tetromino [sprites]
  {:coords {:x 10 :y 15} 
   :sprites sprites
   :positions (cycle sprites)})

;(defn update-positions-in [s positions]
;  (update-in s [:tetromino :positions] positions))

;(defn shift-positions-right [s]
;  (update-in s [:tetromino :positions] #(into (vector (last %)) (pop %))))
;
;(defn shift-positions-left [s]
;  (update-in s [:tetromino :positions] #(conj (subvec % 1) (first %))))

(defn init-bricks [b]
  (->>
    (for [x (range (b :left-x) (inc (b :right-x)))
          y (range (b :bottom-y) (inc (b :top-y)))]
      {:x x :y y})
    (filter #(or (= (% :x) (b :left-x)) (= (% :x) (b :right-x)) (= (% :y) (b :bottom-y))))
    (set)))

(defn init-tetromino []
  (let [all (repeatedly #(tetromino (rand-nth tetris.tetromino/all)))]
    {:current (first all)
     :next (rest all)}))

(defn state []
  {:boundaries (update-in boundaries [:wall-bricks] (fn [x] (init-bricks boundaries)))
   :tetromino (init-tetromino)
   :heap #{}})
