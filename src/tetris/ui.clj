(ns tetris.ui
  (:require
    [tetris.board :as board]
    [lanterna.screen :as term :refer :all]))

(defn draw-board []
  (let [s (term/get-screen)
        b (board/state)]
    (term/start s)
    (doall (map #(term/put-string s (:x %) (- (:top-y (:boundaries b)) (:y %)) "@") (get-in b [:boundaries :wall-bricks])))
    (term/redraw s)
    (term/get-key-blocking s)
    (term/stop s)
    ))
