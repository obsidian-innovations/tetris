(defproject tetris "0.1.0-SNAPSHOT"
  :description ""
  :url ""
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
;  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}
  :plugins [[lein-auto "0.1.1"]]
  :main ^:skip-aot tetris.core
  :target-path "target/%s"
  :repl-options {
                  :init-ns tetris.dev}
  :profiles {
              :uberjar {:aot :all}
              :dev {:dependencies [[org.clojure/tools.namespace "0.2.9"]]}})
