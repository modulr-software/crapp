(ns crapp.config
  (:require [aero.core :as aero]))

(defn- load-config []
  (aero/read-config "config.edn"))

(defn read-value
  "leads in config and uses get-in with ks as an argument"
  [& ks]
  (-> (load-config)
      (get-in ks)))

(comment 
  (read-value :templates)
  (read-value :templates :list)
  ())
