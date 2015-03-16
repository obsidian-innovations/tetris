(ns tetris.ui
  (:require
    [chime :refer [chime-at]]
    [clj-time.core :as t]
    [tetris.board :as board]
    [tetris.core :as core]
    [lanterna.screen :as term :refer :all]))

(defn move-down [state]
  (update-in state [:tetromino :current :coords :y] dec))

(defn move-right [state]
  (update-in state [:tetromino :current :coords :x] inc))

(defn move-left [state]
  (update-in state [:tetromino :current :coords :x] dec))

(defn rotate-left [state]
  (update-in state [:tetromino :current :positions] #(drop (dec (count (get-in state [:tetromino :current :sprites]))) %)))

(defn rotate-right [state]
  (update-in state [:tetromino :current :positions] rest))

(defn move-to-coords [state]
  (core/move-to-xy
    (:x (:coords (:current (:tetromino state))))
    (:y (:coords (:current (:tetromino state))))
    (first (:positions (:current (:tetromino state))))))

(defn move-when-no-collision [state action]
  (let [next-state (action state)]
    (if (core/collision-detected?
          (move-to-coords next-state)
          (clojure.set/union (:heap next-state) (:wall-bricks (:boundaries next-state))))
      state
      next-state)))

(defn do-draw [screen b-updated]
  (let [tetro (:current (:tetromino b-updated))
        tetro-bricks (core/move-to-xy (:x (:coords tetro)) (:y (:coords tetro)) (first (:positions tetro)))
        world (clojure.set/union (:heap b-updated) (:wall-bricks (:boundaries b-updated)))
        all (clojure.set/union tetro-bricks world)]
    (term/clear screen)
    (doall
      (map
        #(term/put-string screen (:x %) (- (:top-y (:boundaries b-updated)) (:y %)) "@")
        all))
    (term/redraw screen)))

(defn put-next-tetromino [state]
  (->
    state
    (update-in [:heap] #(clojure.set/union % (move-to-coords state)))
    (update-in [:heap] #(core/remove-complete-lines 1 19 %))
    (update-in [:heap] #(core/collapse-all-empty 1 14 %))
    (update-in [:tetromino] #(hash-map :current (first (:next %)) :next (rest (:next %))))))

(def action-handlers
  {:move-down move-down :move-left move-left :move-right move-right
   :rotate-clockwise rotate-left :rotate-counter-clockwise rotate-right :do-nothing identity})

(def keypress-to-action
  {:left :move-left :right :move-right :up :rotate-counter-clockwise :down :rotate-clockwise :enter :move-down})

(def event-handlers
  {:user-action keypress-to-action
   :gravity-action (constantly :move-down)})

(defn do-next-board [action-type board]
  (let [action (get action-handlers action-type identity)
        updated-board (move-when-no-collision board action)
        updated-y (get-in updated-board [:tetromino :current :coords :y])
        current-y (get-in board [:tetromino :current :coords :y])]
    (if (and (= current-y updated-y) (= action-type :move-down))
      (put-next-tetromino updated-board)
      updated-board)
    ))

(defn board-timer [screen board events]
  (chime-at [(-> 50 t/millis t/from-now)]
    (fn [time]
      (let [k (term/get-key screen)
            action (reduce apply event-handlers (map vector [(first events) k]))
            board-updated (do-next-board action board)]
        (if (= k :escape)
          (term/stop screen)
          (do
            (do-draw screen board-updated)
            (board-timer screen board-updated (rest events))
            ))
        ))))

(defn event-codes []
  (let [user-action (repeat 30 :user-action)
        gravity-action [:gravity-action]
        init-codes (flatten (interleave gravity-action (vector user-action)))]
    (cycle init-codes)))

(defn draw-board []
  (let [board (board/state)
        events (event-codes)
        screen (term/get-screen)]
    (term/start screen)
    (board-timer screen board events)
    ))

(defn -main [& args]
  (draw-board))
