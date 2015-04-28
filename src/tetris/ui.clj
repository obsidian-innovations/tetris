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

(defn do-draw [screen game]
  (term/clear screen)
  (doall
    (map
      #(term/put-string screen (:x %) (- (:top-y (:walls game)) (:y %)) "@")
      (bricks game)))
  (term/redraw screen))

(defn board-timer [draw-board get-key stop board]
  (chime-at [(-> 50 t/millis t/from-now)]
    (fn [_]
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
        draw-board-fn #(do-draw screen %)
        get-key-fn #(term/get-key screen)
        stop-fn #(term/stop screen)]
    (term/start screen)
    (board-timer draw-board-fn get-key-fn stop-fn board)
    ))

(defn -main [& args]
  (draw-board))
