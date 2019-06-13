(ns hotpot.system
  (:require [aero.core :as aero]
            [clojure.java.io :as io]
            [com.stuartsierra.component :as component]
            [jackdaw.streams :as kstreams]
            [jackdaw.serdes.edn]
            [jackdaw.serdes.json])
  (:import [org.rocksdb DBOptions]))

(defn split-lines
  "Takes an input string and returns a list of words with the
  whitespace removed."
  [input-string]
  (clojure.string/split (clojure.string/lower-case input-string) #" +"))

(comment
  (let [builder (kstreams/streams-builder)
       _ (-> (kstreams/kstream builder {:topic-name "raptors-tweets"
                                        :key-serde (jackdaw.serdes.json/serde)
                                        :value-serde (jackdaw.serdes.json/serde)})
             (kstreams/map-values :Text)
             (kstreams/flat-map-values split-lines)
             (kstreams/group-by (fn [[_ v]] v))
             (kstreams/count)
             (kstreams/to-kstream)
             (kstreams/to {:topic-name "word-count"
                           :key-serde (jackdaw.serdes.json/serde)
                           :value-serde (jackdaw.serdes.json/serde)}))
       app (kstreams/kafka-streams builder
                                   {"application.id" "streams-pipe"
                                    "bootstrap.servers" "kafka:9092"})]
   (kstreams/start app)
   (Thread/sleep 2000)
   (kstreams/close app)))

(defn new-system
  [profile]
  (let [cfg (aero/read-config (io/resource "config.edn")
                              {:profile profile})
        dependency-map {}
        system (component/system-map)]
    (component/system-using system dependency-map)))

(defn dev-system
  []
  (new-system :dev))

(defn docker-system
  []
  (new-system :docker))
