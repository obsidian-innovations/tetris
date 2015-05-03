(ns tetris.ui
  (:require
    [chime :refer [chime-at]]
    [clj-time.core :as t]
    [lanterna.screen :as term :refer :all]
    [tetris.actions.tetromino :refer [move-to-coords]]
    [tetris.actions.common :refer [move-to-xy]]
    [tetris.actions.events :refer :all]
    [tetris.actions.game :refer :all]
    [tetris.actions.events :refer :all]
    [tetris.core.game :as state :refer :all]
    [tetris.core.config :as config]))

(def keypress-to-action
  {:left :move-left
   :right :move-right
   :up :rotate-counter-clockwise
   :down :rotate-clockwise
   :enter :move-down})

(defn- convert-coords-to-screen [screen game coords]
  (let [top-y (:top-y (:walls game))
        [cols rows] (term/get-size screen)
        offset-x (- (/ cols 2) (:board-width config/main))
        offset-y (- (/ rows 2) (/ (:board-heigh config/main) 2))]
    (map #(hash-map :x (+ (* (:x %) 2) offset-x) :y (+ (- top-y (:y %)) offset-y)) coords)))

(defn- print-tetromino-to-screen! [screen game coords charset]
  (let [bricks (convert-coords-to-screen screen game coords)]
    (doall (map #(term/put-string screen (:x %) (:y %) charset) bricks))
    ))

(defn- animate-tetromino-to-screen! [screen game game-old]
  (let [coords-old (move-to-coords game-old)
        coords (move-to-coords game)]
    (if-not (= coords-old coords)
      (do
        (print-tetromino-to-screen! screen game-old coords-old "  ")
        (print-tetromino-to-screen! screen game coords "▒░")))
    ))

(defn- print-next-tetromino!
  ([screen game coords]
   (print-tetromino-to-screen! screen game (move-to-xy -6 (- (:top-y (:walls game)) 5) coords) "▒░"))
  ([screen game]
    (let [coords (-> game (:tetrominos) (:next) (first) (:positions) (first))]
      (print-next-tetromino! screen game coords))))

(defn- clear-next-tetromino!
  ([screen game coords]
   (print-tetromino-to-screen! screen game (move-to-xy -6 (- (:top-y (:walls game)) 5) coords) "  "))
  ([screen game]
   (let [coords (-> game (:tetrominos) (:next) (first) (:positions) (first))]
     (print-next-tetromino! screen game coords))))

(defn- animate-next-tetromino-to-screen! [screen game game-old]
  (let [coords-old (-> game-old (:tetrominos) (:next) (first) (:positions) (first))
        coords (-> game (:tetrominos) (:next) (first) (:positions) (first))]
    (if-not (= coords-old coords)
      (do
        (clear-next-tetromino! screen game-old coords-old)
        (print-next-tetromino! screen game coords)))))

(defn- print-heap-to-screen! [screen game coords charset]
  (let [bricks (convert-coords-to-screen screen game coords)]
    (doall (map #(term/put-string screen (:x %) (:y %) charset) bricks))
    ))

(defn- animate-heap-to-screen! [screen game game-old]
  (let [coords (:heap game)
        coords-old (:heap game-old)]
    (if-not (= coords-old coords)
      (do
        (print-heap-to-screen! screen game-old coords-old "  ")
        (print-heap-to-screen! screen game coords "▒░")
        ))
    ))

(defn- print-walls-to-screen! [screen game]
  (let [coords (get-in game [:walls :wall-bricks])
        bricks (convert-coords-to-screen screen game coords)]
    (doall (map #(term/put-string screen (:x %) (:y %) "▓▓") bricks))
    ))

;http://www.rapidtables.com/code/text/ascii-table.htm
(defn- print-game-to-screen [screen game game-old]
  (let []
    (animate-next-tetromino-to-screen! screen game game-old)
    (animate-tetromino-to-screen! screen game game-old)
    (animate-heap-to-screen! screen game game-old)
    (term/redraw screen)
    ))

(defn schedule-next-move [print-game-fn get-key-fn stop-game-fn game]
  (chime-at [(-> 50 t/millis t/from-now)]
    (fn [_]
      (let [key-pressed (get-key-fn)
            user-action (get keypress-to-action key-pressed :do-nothing)
            game-updated (handle-next-events-batch user-action game)]
        (if (= key-pressed :escape)
          (stop-game-fn)
          (do
            (print-game-fn game-updated game)
            (schedule-next-move print-game-fn get-key-fn stop-game-fn game-updated)))))))

(defn start-game []
  (let [game (state/init-state)
        screen (term/get-screen)
        print-game-fn #(print-game-to-screen screen %1 %2)
        get-key-fn #(term/get-key screen)
        stop-fn #(term/stop screen)]
    (term/start screen)
    (term/clear screen)
    (print-walls-to-screen! screen game)
    (print-next-tetromino! screen game)
    (schedule-next-move print-game-fn get-key-fn stop-fn game)))

(defn -main [& args]
  (start-game))
