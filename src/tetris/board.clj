(ns tetris.board)

(def boundaries {:bottom-y 0
                 :top-y 0
                 :left-x 0
                 :right-x 0})

(def tetromino {:x 0
                :y 0
                :positions []})

(def state {:boundaries boundaries
            :tetromino tetromino
            :heap #{}})


