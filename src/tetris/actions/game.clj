(ns tetris.actions.game
  (:require
    [tetris.core.config :as config]
    [tetris.actions.tetromino :refer :all]
    [tetris.actions.common :refer :all]
    [tetris.actions.events :as es :refer :all]))

(defn get-level [lines-cleared]
  (->>
    (:levels config/main)
    (filter #(<= (:lines-cleared %) lines-cleared))
    (reduce #(if (> (:lines-cleared %1) (:lines-cleared %2)) %1 %2))))

(defn bricks [state]
  (reduce clojure.set/union [(move-to-coords state) (:heap state) (get-in state [:walls :wall-bricks])]))

(defn generate-events-chain [gravity-event-frequency]
  (let [s1 (repeat gravity-event-frequency [:user-action])
        s2 [[:user-action :gravity-action]]]
    (cycle (concat s1 s2))))

(defn generate-events-chain-by-lines-count [count]
  (generate-events-chain
    (:gravity-event-frequency
      (get-level count))))

(defn update-events [game events]
  (assoc-in game [:events] events))

(defn shift-events [state]
  (update-in state [:events] rest))

(defn put-next-tetromino-when-no-collision [game]
  (let [g0 (update-in game [:heap] #(clojure.set/union % (move-to-coords game)))
        cls (completed-lines 1 (:board-width config/main) (:heap g0))]
    (->
      g0
      (update-in [:heap] #(remove-complete-lines % (set (flatten cls))))
      (update-in [:stats :completed-lines-count] #(+ % (count cls)))
      (update-in [:heap] #(collapse-all-empty 1 (:board-heigh config/main) %))
      (move-when-no-collision next-tetromino))))

(defn put-next-when-collision [state-updated state action-type]
  (let [updated-y (get-in state-updated [:tetrominos :current :coords :y])
        current-y (get-in state [:tetrominos :current :coords :y])]
    (if (and (= current-y updated-y) (= action-type :move-down))
      (put-next-tetromino-when-no-collision state-updated)
      state-updated)))

(defn handle-event [event user-action game]
  (let [action-type ((es/event-handlers event) user-action)]
    (-> game
        (move-when-no-collision (es/action-handlers action-type))
        (put-next-when-collision game action-type))))

(defn handle-events [events user-action game]
  (reduce #(handle-event %2 user-action %1) game events))

(defn lines-cleared? [game-updated game]
  (> (lines-count game-updated) (lines-count game)))

(defn handle-next-events-batch [user-action game]
  (let [events (first (:events game))
        game-updated (handle-events events user-action game)]
    (if (lines-cleared? game-updated game)
      (update-events game-updated (generate-events-chain-by-lines-count (lines-count game-updated)))
      (shift-events game-updated))))
