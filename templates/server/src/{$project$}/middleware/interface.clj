(ns {$project$}.middleware.interface
  (:require [{$project$}.middleware.core :as mw]))

(defn apply-generic [app]
  (mw/apply-generic app))
