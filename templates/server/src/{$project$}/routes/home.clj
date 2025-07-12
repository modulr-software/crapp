(ns {$project$}.routes.home
  (:require [ring.util.response :as res]))

(defn get [request]
  (res/response {:message "Hello, world!"}))
