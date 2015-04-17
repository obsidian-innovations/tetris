(ns tetris.actions.events
  (:require
    [tetris.actions.tetromino :refer :all]))

(def action-handlers
  {:move-down move-down
   :move-left move-left
   :move-right move-right
   :rotate-clockwise rotate-left
   :rotate-counter-clockwise rotate-right
   :do-nothing identity})

(def keypress-to-action
  {:left :move-left
   :right :move-right
   :up :rotate-counter-clockwise
   :down :rotate-clockwise
   :enter :move-down})

(def event-handlers
  {:user-action keypress-to-action
   :gravity-action (constantly :move-down)})
