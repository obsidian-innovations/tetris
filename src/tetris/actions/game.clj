(ns tetris.actions.game
  (:require
    [tetris.actions.tetromino :refer :all]
    [tetris.actions.common :refer :all]
    [tetris.actions.events :as es :refer :all]))

(defn bricks [state]
  (reduce clojure.set/union [(move-to-coords state) (:heap state) (get-in state [:walls :wall-bricks])]))

(defn shift-events [state]
  (update-in state [:events] rest))

(defn put-next-tetromino-when-no-collision [state]
  (->
    state
    (update-in [:heap] #(clojure.set/union % (move-to-coords state)))
    (update-in [:heap] #(remove-complete-lines 1 19 %))
    (update-in [:heap] #(collapse-all-empty 1 14 %))
    (move-when-no-collision next-tetromino)))

(defn put-next-when-collision [state-updated state action-type]
  (let [updated-y (get-in state-updated [:tetrominos :current :coords :y])
        current-y (get-in state [:tetrominos :current :coords :y])]
    (if (and (= current-y updated-y) (= action-type :move-down))
      (put-next-tetromino-when-no-collision state-updated)
      state-updated)))


(defn handle-next-event [user-action game]
  (let [event (first (:events game))
        action-type ((es/event-handlers event) user-action)]
    (-> game
      (move-when-no-collision (es/action-handlers action-type))
      (put-next-when-collision game action-type)
      (shift-events))))
