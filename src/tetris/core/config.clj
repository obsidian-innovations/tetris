(ns tetris.core.config)

(def main
  {:board-width 10
   :board-heigh 20
   :gravity-event-frequency 15
   :levels
    [{:lines-cleared  0 :gravity-event-frequency 15 :level 0}
     {:lines-cleared 15 :gravity-event-frequency 13 :level 1}
     {:lines-cleared 25 :gravity-event-frequency 11 :level 2}
     {:lines-cleared 35 :gravity-event-frequency  9 :level 3}
     {:lines-cleared 45 :gravity-event-frequency  7 :level 4}
     {:lines-cleared 55 :gravity-event-frequency  6 :level 5}
     {:lines-cleared 65 :gravity-event-frequency  5 :level 6}
     {:lines-cleared 75 :gravity-event-frequency  4 :level 7}
     {:lines-cleared 85 :gravity-event-frequency  3 :level 8}]})
