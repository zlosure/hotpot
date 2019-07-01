(ns hotpot.system
  (:require [aero.core :as aero]
            [clojure.java.io :as io]
            [com.stuartsierra.component :as component]
            [hotpot.worker :as worker])
  (:import [org.rocksdb DBOptions]))

(defn new-system
  [profile]
  (let [cfg (aero/read-config (io/resource "config.edn")
                              {:profile profile})
        dependency-map {}
        system (component/system-map :worker (worker/new-worker))]
    (component/system-using system dependency-map)))

(defn dev-system
  []
  (new-system :dev))

(defn docker-system
  []
  (new-system :docker))
