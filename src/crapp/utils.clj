(ns crapp.utils
  (:import (java.io File)))

(defn list-files [path]
  (.listFiles (File. path)))

(defn filename [file]
  (.getName file))

(defn is-directory [file]
  (.isDirectory file))

(defn make-directory! [path]
  (.mkdir (File. path)))

(defn append-file! [path content]
  (spit path content :append true))
