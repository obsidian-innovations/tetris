(ns tetris.ui
  (:require
    [chime :refer [chime-at]]
    [clj-time.core :as t]
    [tetris.board :as board]
    [tetris.core :as core]
    [lanterna.screen :as term :refer :all]))

(defn move-down [state]
  (update-in state [:tetromino :coords :y] dec))

(defn move-right [state]
  (update-in state [:tetromino :coords :x] inc))

(defn move-left [state]
  (update-in state [:tetromino :coords :x] dec))

(defn rotate-left [state]
  (update-in state [:tetromino :positions] #(drop (dec (count (get-in state [:tetromino :sprites]))) %)))

(defn rotate-right [state]
  (update-in state [:tetromino :positions] rest))

(defn move-when-no-collision [state action]
  (let [next-state (action state)]
    (if (core/collision-detected?
          (core/move-to-xy
            (:x (:coords (:tetromino next-state)))
            (:y (:coords (:tetromino next-state)))
            (first (:positions (:tetromino next-state))))
          (clojure.set/union (:heap next-state) (:wall-bricks (:boundaries next-state))))
      state
      next-state)))

(defn do-draw [screen b-updated on-done-fn]

  (let [tetro (:tetromino b-updated)
        tetro-bricks (core/move-to-xy (:x (:coords tetro)) (:y (:coords tetro)) (first (:positions tetro)))
        world (clojure.set/union (:heap b-updated) (:wall-bricks (:boundaries b-updated)))
        all (clojure.set/union tetro-bricks world)
        s screen]

    (term/clear s)
    
    (doall
      (map
        #(term/put-string s (:x %) (- (:top-y (:boundaries b-updated)) (:y %)) "@")
        all))

    (term/redraw s)
    
    (on-done-fn b-updated)
    ))

(def event-handlers
  {:user-action
   (fn [screen board events on-draw-done-fn]
     (let [k (term/get-key screen)]
       (cond
         (= k :escape) (term/stop screen)
         (= k :left) (do-draw screen (move-when-no-collision board move-left) #(on-draw-done-fn screen % events))
         (= k :right) (do-draw screen (move-when-no-collision board move-right) #(on-draw-done-fn screen % events))
         (= k :space) (do-draw screen (move-when-no-collision board move-down) #(on-draw-done-fn screen % events))
         (= k :up) (do-draw screen (move-when-no-collision board rotate-left) #(on-draw-done-fn screen % events))
         (= k :down) (do-draw screen (move-when-no-collision board rotate-right) #(on-draw-done-fn screen % events))
         :else (on-draw-done-fn screen board events)
         ;:else (do-draw screen (move-when-no-collision board move-down) #(board-timer screen %))
         )))
   :gravity-action
     (fn [screen board events on-draw-done-fn]
       (do-draw
         screen
         (move-when-no-collision board move-down)
         #(on-draw-done-fn screen % events)))
   })

(defn board-timer [screen board events]
  (chime-at [(-> 50 t/millis t/from-now)]
    (fn [time]
      (((first events) event-handlers) screen board (rest events) board-timer)
      ))
  )

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
