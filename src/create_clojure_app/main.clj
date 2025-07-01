(ns create-clojure-app.main
  (:require [create-clojure-app.template :as template]
            [create-clojure-app.utils :as utils]))

(defn create-project! [project-name template]
  (println (str "Creating " template " project '" project-name "'..."))
  (try
    (let [project-path (str "./" project-name)
          template-path (str "./templates/" template)]
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

    (some #(= (last args) %) ["default" "server"])
    (create-project! (first args) (last args))

    (> (count args) 2)
    (println "Too many arguments.")))
