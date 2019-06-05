(defproject hotpot "0.1.0-SNAPSHOT"
  :description ""
  :url ""
  :license {:name ""
            :url ""}
  :dependencies [[aero "1.0.3"]
                 [com.stuartsierra/component "0.4.0"]
                 [com.taoensso/timbre "4.10.0"]
                 [fundingcircle/jackdaw "0.6.4"]
                 [io.aeron/aeron-all "1.17.0"]
                 [nrepl "0.6.0"]
                 [org.clojure/clojure "1.10.0"]
                 [org.clojure/tools.cli "0.3.5"]
                 [org.onyxplatform/onyx "0.14.1"]
                 [org.onyxplatform/lib-onyx "0.14.0.0"]
                 [prismatic/schema "1.1.10"]]
  :source-paths ["src"]

  :profiles {:dev {:jvm-opts ["-XX:-OmitStackTraceInFastThrow"]
                   :global-vars {*assert* true}
                   :dependencies [[org.clojure/tools.namespace "0.2.11"]
                                  [lein-project-version "0.1.0"]
                                  [reloaded.repl "0.2.4"]]
                   :plugins [[cider/cider-nrepl "0.21.1"]]
                   :repl-options {:host "0.0.0.0"
                                  :port 55555
                                  :timeout 3000}
                   :source-paths ["dev"]}

             :uberjar {:aot [lib-onyx.media-driver
                             hotpot.core]
                       :uberjar-name "peer.jar"
                       :global-vars {*assert* false}}})
