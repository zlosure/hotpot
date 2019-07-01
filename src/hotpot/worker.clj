(ns hotpot.worker
  (:require [com.stuartsierra.component :as component]
            [jackdaw.serdes.edn]
            [jackdaw.serdes.json]
            [jackdaw.streams :as kstreams]
            [taoensso.timbre :as timbre]))

(defrecord Worker [kafka-streams]
  component/Lifecycle
  (start [this]
    (timbre/info ::start :kafka-streams kafka-streams)
    (if kafka-streams
      this
      (let [builder (kstreams/streams-builder)
            _ (-> (kstreams/kstream builder {:topic-name "kawhi-tweets-raw"
                                             :key-serde (jackdaw.serdes.json/serde)
                                             :value-serde (jackdaw.serdes.json/serde)})
                  (kstreams/map-values :Text)
                  (kstreams/to {:topic-name "kawhi-tweets"
                                :key-serde (jackdaw.serdes.json/serde)
                                :value-serde (jackdaw.serdes.json/serde)}))
            kafka-streams (kstreams/kafka-streams builder
                                                  {"application.id" "streams-pipe"
                                                   "bootstrap.servers" "kafka:9092"})]
        (kstreams/start kafka-streams)
        (assoc this :kafka-streams kafka-streams))))
  (stop [this]
    (timbre/info ::stop :kafka-streams kafka-streams)
    (some-> kafka-streams kstreams/close)
    (dissoc this :kafka-streams)))

(defn new-worker
  []
  (map->Worker {}))
