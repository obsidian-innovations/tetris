(ns tetris.ui
  (:require
    [chime :refer [chime-at]]
    [clj-time.core :as t]
    [lanterna.screen :as term :refer :all]
    [tetris.actions.common :refer [move-to-xy]]
    [tetris.actions.events :refer :all]
    [tetris.actions.game :refer :all]
    [tetris.actions.events :refer :all]
    [tetris.core.game :as state :refer :all]))

(defn do-draw [screen b-updated]
  (let [tetro (:current (:tetrominos b-updated))
        tetro-bricks (move-to-xy (:x (:coords tetro)) (:y (:coords tetro)) (first (:positions tetro)))
        world (clojure.set/union (:heap b-updated) (:wall-bricks (:walls b-updated)))
        all (clojure.set/union tetro-bricks world)]
    (term/clear screen)
    (doall
      (map
        #(term/put-string screen (:x %) (- (:top-y (:walls b-updated)) (:y %)) "@")
        all))
    (term/redraw screen)))



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
  (let [board (state/init-state)
        screen (term/get-screen)
        draw-board #(do-draw screen %)
        get-key #(term/get-key screen)
        stop #(term/stop screen)]
    (term/start screen)
    (board-timer draw-board get-key stop board)
    ))

(defn -main [& args]
  (draw-board))
