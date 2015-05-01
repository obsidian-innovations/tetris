(ns tetris.ui
  (:require
    [chime :refer [chime-at]]
    [clj-time.core :as t]
    [lanterna.screen :as term :refer :all]
    [tetris.actions.tetromino :refer [move-to-coords]]
    [tetris.actions.common :refer [move-to-xy]]
    [tetris.actions.events :refer :all]
    [tetris.actions.game :refer :all]
    [tetris.actions.events :refer :all]
    [tetris.core.game :as state :refer :all]))

(def keypress-to-action
  {:left :move-left
   :right :move-right
   :up :rotate-counter-clockwise
   :down :rotate-clockwise
   :enter :move-down})

(defn- convert-coords-to-screen [game coords]
  (let [top-y (:top-y (:walls game))]
    (map #(hash-map :x (* (:x %) 2) :y (- top-y (:y %))) coords)))

(defn- print-tetromino-to-screen [screen game]
  (let [coords (move-to-coords game)
        bricks (convert-coords-to-screen game coords)]
    (doall (map #(term/put-string screen (:x %) (:y %) "▒░") bricks))
    ))

(defn- print-heap-to-screen [screen game]
  (let [coords (get-in game [:heap])
        bricks (convert-coords-to-screen game coords)]
    (doall (map #(term/put-string screen (:x %) (:y %) "▒░") bricks))
    ))

(defn- print-walls-to-screen [screen game]
  (let [coords (get-in game [:walls :wall-bricks])
        bricks (convert-coords-to-screen game coords)]
    (doall (map #(term/put-string screen (:x %) (:y %) "▓▓") bricks))
    ))

;http://www.rapidtables.com/code/text/ascii-table.htm
(defn- print-game-to-screen [game screen]
  (let []
    (term/clear screen)
    (print-walls-to-screen screen game)
    (print-heap-to-screen screen game)
    (print-tetromino-to-screen screen game)
    ;(doall
    ;  (map
    ;    #(term/put-string screen (* (:x %) 2) (- (:top-y (:walls game)) (:y %)) "▒░")
    ;    (bricks game)))
    (term/redraw screen)
    ))

(defn schedule-next-move [print-game-fn get-key-fn stop-game-fn game]
  (chime-at [(-> 40 t/millis t/from-now)]
    (fn [_]
      (let [key-pressed (get-key-fn)
            user-action (get keypress-to-action key-pressed :do-nothing)
            game-updated (handle-next-event user-action game)]
        (if (= key-pressed :escape)
          (stop-game-fn)
          (do
            (print-game-fn game-updated)
            (schedule-next-move print-game-fn get-key-fn stop-game-fn game-updated)))))))

(defn start-game []
  (let [board (state/init-state)
        screen (term/get-screen)
        print-game-fn #(print-game-to-screen % screen)
        get-key-fn #(term/get-key screen)
        stop-fn #(term/stop screen)]
    (term/start screen)
    ;(term/clear screen)
    ;(print-walls-to-screen screen board)
    (schedule-next-move print-game-fn get-key-fn stop-fn board)))

(defn -main [& args]
  (start-game))
