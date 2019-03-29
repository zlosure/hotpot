(ns hotpot.system
  (:require [aero.core :as aero]
            [clojure.java.io :as io]
            [com.stuartsierra.component :as component]
            [hotpot.aeron :as aeron]))

(defn new-system
  [profile]
  (let [cfg (aero/read-config (io/resource "config.edn")
                              {:profile profile})
        dependency-map {}
        system (component/system-map
                :aeron (aeron/new-aeron (:aeron cfg)))]
    (component/system-using system dependency-map)))

(defn dev-system
  []
  (new-system :dev))

(defn docker-system
  []
  (new-system :docker))
