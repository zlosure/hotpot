(ns hotpot.system
  (:require [aero.core :as aero]
            [clojure.java.io :as io]
            [com.stuartsierra.component :as component]
            [hotpot.aeron :as aeron]
            [jackdaw.streams :as kstreams]))

(comment
  (let [builder (kstreams/streams-builder)
       _ (-> (kstreams/kstream builder {:topic-name "test"
                                        :value-serde (jackdaw.serdes.edn/serde)})
             (kstreams/flat-map-values (fn [m]
                                         (prn m)
                                         m))
             (kstreams/to {:topic-name "test-output"
                           :value-serde (jackdaw.serdes.edn/serde)}))
       app (kstreams/kafka-streams builder
                                   {"application.id" "streams-pipe"
                                    "bootstrap.servers" "kafka:9092"})]
   (kstreams/start app)
   (Thread/sleep 1000)
   (kstreams/close app)))

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
