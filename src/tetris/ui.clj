(ns tetris.ui
  (:require
    [chime :refer [chime-at]]
    [clj-time.core :as t]
    [tetris.board :as board]
    [lanterna.screen :as term :refer :all]))

(defn do-draw [screen on-done-fn]

  (let [b (board/state)
        s screen]

    (doall
      (map
        #(term/put-string s (:x %) (- (:top-y (:boundaries b)) (:y %)) "@")
        (get-in b [:boundaries :wall-bricks])))
    
    (term/redraw s)
    
    (on-done-fn)
    )
  )

(defn board-timer [count screen]
  
  (chime-at [(-> 1 t/secs t/from-now)]
    (fn [time]
      (println "hello")
      (if (> count 0)

        (do-draw screen #(board-timer (dec count) screen))

        (do
          (term/get-key-blocking screen)
          (term/stop screen))
        )
      
      ))
  
  )

(defn draw-board [count]

  (let [s (term/get-screen)]
    (term/start s)

    (board-timer count s)
    
    
    ))
