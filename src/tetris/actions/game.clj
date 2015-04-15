(ns tetris.actions.game)

(defn shift-events [state]
  (update-in state [:events] rest))

(defn do-next-board [action-type board]
  (let [action (get action-handlers action-type identity)]
    (-> board
      (move-when-no-collision action)
      (put-next-when-collision board action-type)
      (shift-events))))

(defn put-next-tetromino-when-no-collision [state]
  (->
    state
    (update-in [:heap] #(clojure.set/union % (move-to-coords state)))
    (update-in [:heap] #(core/remove-complete-lines 1 19 %))
    (update-in [:heap] #(core/collapse-all-empty 1 14 %))
    (move-when-no-collision next-tetromino)))

(defn put-next-when-collision [state-updated state action-type]
  (let [updated-y (get-in state-updated [:tetrominos :current :coords :y])
        current-y (get-in state [:tetrominos :current :coords :y])]
    (if (and (= current-y updated-y) (= action-type :move-down))
      (put-next-tetromino-when-no-collision state-updated)
      state-updated)))
