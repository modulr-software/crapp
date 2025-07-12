(ns {$project$}.middleware.core
  (:require [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.middleware.cookies :refer [wrap-cookies]]))

(defn apply-generic [app]
  (-> app
      (wrap-params)
      (wrap-json-response)
      (wrap-json-body {:keywords? true})
      (wrap-cookies)))
