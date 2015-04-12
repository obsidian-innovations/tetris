(ns tetris.ui
  (:require
    [chime :refer [chime-at]]
    [clj-time.core :as t]
    [lanterna.screen :as term :refer :all]
    [tetris.core :as core :refer :all]
    [tetris.board :as game :refer :all])
  ;(:gen-class)
  )

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

(defn shift-events [state]
  (update-in state [:events] rest))

(defn next-tetromino [state]
  (update-in state [:tetrominos] #(hash-map :current (first (:next %)) :next (rest (:next %)))))

(defn move-to-coords [state]
  (core/move-to-xy
    (:x (:coords (:current (:tetrominos state))))
    (:y (:coords (:current (:tetrominos state))))
    (first (:positions (:current (:tetrominos state))))))

(defn move-when-no-collision [state action]
  (let [next-state (action state)]
    (if (core/collision-detected?
          (move-to-coords next-state)
          (clojure.set/union (:heap next-state) (:wall-bricks (:walls next-state))))
      state
      next-state)))

(defn do-draw [screen b-updated]
  (let [tetro (:current (:tetrominos b-updated))
        tetro-bricks (core/move-to-xy (:x (:coords tetro)) (:y (:coords tetro)) (first (:positions tetro)))
        world (clojure.set/union (:heap b-updated) (:wall-bricks (:walls b-updated)))
        all (clojure.set/union tetro-bricks world)]
    (term/clear screen)
    (doall
      (map
        #(term/put-string screen (:x %) (- (:top-y (:walls b-updated)) (:y %)) "@")
        all))
    (term/redraw screen)))

(defn put-next-tetromino-when-no-collision [state]
  (->
    state
    (update-in [:heap] #(clojure.set/union % (move-to-coords state)))
    (update-in [:heap] #(core/remove-complete-lines 1 19 %))
    (update-in [:heap] #(core/collapse-all-empty 1 14 %))
    (move-when-no-collision next-tetromino)))

(def action-handlers
  {:move-down move-down :move-left move-left :move-right move-right
   :rotate-clockwise rotate-left :rotate-counter-clockwise rotate-right :do-nothing identity})

(def keypress-to-action
  {:left :move-left :right :move-right :up :rotate-counter-clockwise :down :rotate-clockwise :enter :move-down})

(def event-handlers
  {:user-action keypress-to-action
   :gravity-action (constantly :move-down)})

(defn put-next-when-collision [state-updated state action-type]
  (let [updated-y (get-in state-updated [:tetrominos :current :coords :y])
        current-y (get-in state [:tetrominos :current :coords :y])]
    (if (and (= current-y updated-y) (= action-type :move-down))
      (put-next-tetromino-when-no-collision state-updated)
      state-updated)))

(defn do-next-board [action-type board]
  (let [action (get action-handlers action-type identity)]
    (-> board
      (move-when-no-collision action)
      (put-next-when-collision board action-type)
      (shift-events))))

(defn board-timer [draw-board get-key stop board]
  (chime-at [(-> 50 t/millis t/from-now)]
    (fn [time]
      (let [key (get-key)
            current-event (first (:events board))
            action (reduce apply event-handlers (map vector [current-event key]))
            board-updated (do-next-board action board)]
        (if (= key :escape)
          (stop)
          (do
            (draw-board board-updated)
            (board-timer draw-board get-key stop board-updated)
            ))
        ))))

(defn draw-board []
  (let [board (game/init-state)
        screen (term/get-screen)
        draw-board #(do-draw screen %)
        get-key #(term/get-key screen)
        stop #(term/stop screen)]
    (term/start screen)
    (board-timer draw-board get-key stop board)
    ))

(defn -main [& args]
  (draw-board))
