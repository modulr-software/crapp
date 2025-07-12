(ns {$project$}.server
  (:require [org.httpkit.server :as http]
            [{$project$}.routes.interface :as routes]))

(defonce ^:private *server (atom nil))

(defn running? []
  (some? @*server))

(defn start-server []
  (cond (not (some? @*server))
        (do
          (println "Starting server on port 3000...")
          (reset! *server (http/run-server
                           (routes/create-app)
                           {:port 3000})))
        :else
        (println "Server already running!")))

(defn stop-server []
  (println "Stopping server...")
  (when (some? @*server)
    (@*server))
  (reset! *server nil))

(defn restart-server []
  (stop-server)
  (start-server))
