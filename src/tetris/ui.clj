(ns tetris.ui
  (:require [lanterna.screen :as term :refer :all]))

(defn draw-board []
  (let [s (term/get-screen)]
    (term/start s)
    (term/put-string s 10 10 "Hello, world!")
    (term/redraw s)
    (term/get-key-blocking s)
    (term/stop s)
    
    ))
