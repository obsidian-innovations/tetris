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

(def event-handlers
  {:user-action identity
   :gravity-action (constantly :move-down)})
