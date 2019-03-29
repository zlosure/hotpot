(ns hotpot.aeron
  "Running Aeron media driver as a sub-process of Onyx seems to be not
  recommanded by Onyx. However, I can't see the reason behind it.
  1. First of all, media drvier only needs 3 threads at maximum. It can be
  handled by sharing JVM with Onyx.
  2. Media driver and Onyx kind of live and die together, it makes no sense to
  separate them and monitor each other.
  Therefore, I'll make it embedded as a component unless obvious evidences are
  shown."
  (:require [clojure.core.async :as async]
            [com.stuartsierra.component :as component]
            [hotpot.util :as util]
            [schema.core :as s]
            [taoensso.timbre :as timbre])
  (:import [io.aeron.driver MediaDriver MediaDriver$Context]))

(defrecord Aeron [blocking-chan
                  media-driver
                  properties]
  component/Lifecycle
  (start [this]
    (timbre/info ::start
                 :media-driver media-driver
                 :blocking-chan blocking-chan
                 :properties properties)
    (if media-driver
      this
      (let [;; Add media driver properties config into system properties.
            _ (run! (fn [[k v]] (System/setProperty k v)) properties)
            ctx (MediaDriver$Context.)
            media-driver (MediaDriver/launch ctx)
            blocking-chan (async/chan)]
        (timbre/info ::start "Launched the Media Driver. Blocking forever...")
        (assoc this
               :media-driver media-driver
               :blocking-chan blocking-chan)
        (async/<!! blocking-chan))))
  (stop [this]
    (timbre/info ::stop
                 :media-driver media-driver
                 :blocking-chan blocking-chan
                 :properties properties)
    (some-> media-driver .close)
    (some-> blocking-chan async/close!)
    (run! (fn [[k v]] (System/clearProperty k)) properties)
    (timbre/info ::stop "Shutted down the Media Driver.")
    (dissoc this :media-driver :blocking-chan)))

(s/defschema ^:private Config
  {:config-uri (s/maybe s/Str)})

(s/defn new-aeron
  [cfg :- Config]
  (let [{:keys [config-uri]} cfg
        properties (when config-uri (util/file->properties-map config-uri))]
    (map->Aeron {:properties properties})))
