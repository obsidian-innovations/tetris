(ns tetris.tetromino)

(def i-like
    [#{{:x 0 :y 0} {:x -1 :y 0} {:x 1 :y 0} {:x 2 :y 0}}
     #{{:x 0 :y 0} {:x 0 :y 1} {:x 0 :y -1} {:x 0 :y -2}}
     #{{:x 0 :y 0} {:x 1 :y 0} {:x -1 :y 0} {:x -2 :y 0}}
     #{{:x 0 :y 0} {:x 0 :y -1} {:x 0 :y 1} {:x 0 :y 2}}])

(def j-like 
    [#{{:x 0 :y 0} {:x 1 :y 0} {:x 2 :y 0} {:x 0 :y 1}}])

(def l-like 
    [#{{:x 0 :y 0} {:x 1 :y 0} {:x 2 :y 0} {:x 2 :y 1}}])

(def o-like
    [#{{:x 0 :y 0} {:x 1 :y 0} {:x 0 :y 1} {:x 1 :y 1}}])

(def s-like 
    [#{{:x 0 :y 0} {:x 1 :y 0} {:x 1 :y 1} {:x 2 :y 2}}])

(def t-like 
    [#{{:x 0 :y 0} {:x 1 :y 0} {:x -1 :y 0} {:x 0 :y 1}}
     #{{:x 0 :y 0} {:x 0 :y 1} {:x 1 :y 0} {:x 0 :y -1}}
     #{{:x 0 :y 0} {:x 1 :y 0} {:x -1 :y 0} {:x 0 :y -1}}
     #{{:x 0 :y 0} {:x 0 :y 1} {:x -1 :y 0} {:x 0 :y -1}}])

(def z-like 
    [#{{:x 0 :y 0} {:x 0 :y 1} {:x -1 :y 1} {:x 1 :y 0}}
     #{{:x 0 :y 0} {:x 1 :y 0} {:x 1 :y 1} {:x 0 :y -1}}])

(def all-objects 
  [i-like j-like l-like o-like s-like t-like z-like])
