(ns tetris.board)

(def boundaries {:bottom-y 0
                 :top-y 0
                 :left-x 0
                 :right-x 0
                 :wall-bricks #{}})

(def tetromino {:x 0
                :y 0
                :positions []})

(defn update-positions-in [s positions]
  (update-in s [:tetromino :positions] positions))

(defn shift-positions-right [s]
  (update-in s [:tetromino :positions] #(into (vector (last %)) (pop %))))

(defn shift-positions-left [s]
  (update-in s [:tetromino :positions] #(conj (subvec % 1) (first %))))

(defn init-bricks [b]
  (set
    (for [x (range (b :left-x) (inc (b :right-x)))
          y (range (b :bottom-y) (inc (b :top-y)))]
      {:x x :y y})))

(defn state []
  {:boundaries (update-in boundaries [:wall-bricks] #(init-bricks boundaries))
   :tetromino tetromino
   :heap #{}})
