(ns hotpot.aeron-media-driver
  (:require [clojure.core.async :as async]
            [com.stuartsierra.component :as component]
            [taoensso.timbre :as timbre])
  (:import [io.aeron.driver MediaDriver MediaDriver$Context ThreadingMode]))

(defrecord AeronMediaDriver [blocking-chan
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
      (let [ctx (MediaDriver$Context.)
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
    (timbre/info ::stop "Shutted down the Media Driver.")
    (dissoc this :media-driver :blocking-chan)))

(let [ctx (doto (MediaDriver$Context.)
            (.aeronDirectoryName "/dev/shm/aeron-dir")
            (.dirDeleteOnStart true)
            (.mtuLength 16384)
            (.threadingMode ThreadingMode/DEDICATED))
      media-driver (MediaDriver/launch ctx)]
  (prn (.aeronDirectoryName media-driver))
  (prn (.context media-driver))
  (.close media-driver))
