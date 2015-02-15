(ns tetris.core
  (require [clojure.tools.namespace.repl :refer [refresh]])
  (use [clojure.test])
  (:gen-class))


(def o-like
    #{{:x 0 :y 0} {:x 1 :y 0} {:x 0 :y 1} {:x 1 :y 1}})

(defn place-to [x y obj]
  (set
    (map #(hash-map :x (+ (:x %) x) :y (+ (:y %) y)) obj)))

(deftest placing-objects
  (is (= #{{:x 10 :y 10} {:x 11 :y 10} {:x 10 :y 11} {:x 11 :y 11}} (place-to 10 10  o-like))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
