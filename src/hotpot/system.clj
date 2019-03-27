(ns hotpot.system
  (:require [com.stuartsierra.component :as component]))

(defn new-system
  [profile])

(defn dev-system
  []
  (new-system :dev))

(defn docker-system
  []
  (new-system :docker))
