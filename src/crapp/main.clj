(ns crapp.main
  (:require [crapp.template :as template]
            [crapp.utils :as utils]
            [crapp.config :as conf]))

(defn create-project! [project-name template]
  (println (str "Creating " template " project '" project-name "'..."))
  (try
    (let [project-path (str "./" project-name)
          template-path (str (conf/read-value :templates :dir) "/" template)]
      (utils/make-directory! project-path)
      (template/deploy-template-files! template-path 
                                       project-path 
                                       {:project project-name}))
    (catch Exception e
      (println (.getMessage e)))
    (finally 
      (println "Done."))))

(defn -main [& args]
  (cond
    (empty? args)
    (println "Please provide a project name.")

    (= (count args) 1)
    (create-project! (first args) "default")

    (some #(= (last args) %) (conf/read-value :templates :list))
    (create-project! (first args) (last args))

    (> (count args) 2)
    (println "Too many arguments.")))
