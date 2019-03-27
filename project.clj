(defproject hotpot "0.1.0-SNAPSHOT"
  :description ""
  :url ""
  :license {:name ""
            :url ""}
  :dependencies [[aero "1.0.3"]
                 [com.stuartsierra/component "0.4.0"]
                 [com.taoensso/timbre "4.10.0"]
                 [nrepl "0.6.0"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.cli "0.3.5"]
                 [org.onyxplatform/onyx "0.14.1"]
                 [org.onyxplatform/lib-onyx "0.14.0.0"]]
  :source-paths ["src"]

  :profiles {:dev {:jvm-opts ["-XX:-OmitStackTraceInFastThrow"]
                   :global-vars {*assert* true}
                   :dependencies [[org.clojure/tools.namespace "0.2.11"]
                                  [lein-project-version "0.1.0"]]
                   :plugins [[cider/cider-nrepl "0.21.1"]]
                   :repl-options {:host "0.0.0.0"
                                  :port 55555
                                  :timeout 3000}}

             :uberjar {:aot [lib-onyx.media-driver
                             hotpot.core]
                       :uberjar-name "peer.jar"
                       :global-vars {*assert* false}}})
