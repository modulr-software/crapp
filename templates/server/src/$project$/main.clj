(ns $project$.$project$
  (:require [$project$.server :as server]
            [$project$.hooks :as hooks]))

(defn -main [& _]
  (server/start-server)
  (hooks/add-shutdown-hook server/stop-server))
