(ns user
  (:require [reloaded.repl :refer [system init start stop go reset reset-all]]
            [hotpot.system :as app]))

(reloaded.repl/set-init! app/dev-system)
