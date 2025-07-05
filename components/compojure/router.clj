(ns $project$.routes.router
  (:require [compojure.core :refer [routes GET]]
            [compojure.route :as route]
            [$project$.middleware.interface :as mw]
            [$project$.routes.home :as home]))

(defn create-app []
  (-> 
    (routes 
      (GET "/" request [] (home/get request)))
    (mw/apply-generic)))
