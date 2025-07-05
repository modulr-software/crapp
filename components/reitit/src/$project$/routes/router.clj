(ns $project$.routes.router
  (:require [reitit.ring :as ring]
            [$project$.middleware.interface :as mw]
            [$project$.routes.home :as home]))

(defn create-app []
  (ring/ring-handler
    (ring/router 
      [["/" {:middleware [[mw/apply-generic]]}
        ["" {:get home/get}]]])))
