(ns tetris.core
  (:gen-class))

(def o-like
    #{{:x 0 :y 0} {:x 1 :y 0} {:x 0 :y 1} {:x 1 :y 1}})

(defn place-to [x y obj]
  (map #(hash-map :x (+ (:x %) x) :y (+ (:y %) y)) obj))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
