(ns crapp.template
  (:require [clojure.string :as string]
            [camel-snake-kebab.core :as csk]
            [crapp.utils :as utils]
            [crapp.config :as conf]))

(defn compile-deps
  "provided the template (keyword) and components (keyword vec), returns a string 
  of the final list of dependencies in deps.edn format.
  e.g.
  :deps {first/dependency {:mvn/version ###}
         second/dependency {:mvn/version ###}}"
  [template components]
  (let [template-deps (string/join "\n" (conf/read-value :templates :list template :deps))
        components-deps (->> components
                             (mapv #(conf/read-value :components :list % :deps))
                             (flatten)
                             (vec)
                             (string/join \newline))]
    (str ":deps {" template-deps \newline components-deps "}")))

(defn parse-template [{:keys [template values]}]
  (string/join (map
                (fn [part]
                  (or (get values (keyword part)) part))
                (string/split template #"\{\$|\$\}"))))

(defn deploy-template-files! [basepath targetpath values]
  (doseq [{:keys [filename is-directory]} (mapv
                                           (fn [f]
                                             {:filename (utils/filename f)
                                              :is-directory (utils/is-directory f)})
                                           (utils/list-files basepath))]

    (if is-directory

      (let [dirname (->> filename
                         (assoc {:values values} :template)
                         (parse-template)
                         (csk/->snake_case))]
        (utils/make-directory! (str targetpath "/" dirname))
        (deploy-template-files! (str basepath "/" filename)
                                (str targetpath "/" dirname)
                                values))

      (let [original (slurp (str basepath "/" filename))
            replaced (parse-template {:template original
                                      :values values})]
        (spit (str targetpath "/" filename) replaced)))))

(defn create-project! [project-name template components]
  (println (str "\nCreating " (name template) " project '" project-name "'..."))
  (try
    (let [project-path (str "./" project-name)
          template-path (str (conf/read-value :templates :dir) "/" (name template))]

      (utils/make-directory! project-path)
      (deploy-template-files! template-path
                              project-path
                              {:project project-name})

      (doseq [c components]
        (let [component-path (str (conf/read-value :components :dir) "/" (name c))]
          (deploy-template-files! component-path
                                  project-path
                                  {:project project-name})))

      (utils/append-file! (str project-path "/deps.edn")
                          (str (compile-deps template components) "}")))

    (catch Exception e
      (println (.getMessage e)))
    (finally
      (println "Done."))))

(comment
  (let [dir (utils/list-files "./templates/default")]
    (mapv (fn [f]
            {:filename (utils/filename f)
             :is-directory (utils/is-directory f)}) dir))

  (deploy-template-files! "./templates/server" "./testing" {:project "my-server-app"})
  (deploy-template-files! "./components/reitit" "./testing" {:project "my-server-app"})

  (utils/append-file! "testdeps.edn" (str (compile-deps :server [:reitit :compojure]) \}))

  (create-project! "my-server-app" :server [:reitit])

  (->> "project"
       (assoc {:values {:project "my-app"}} :template)
       (parse-template)
       (csk/->snake_case)
       (str "./templates/default" "/"))

  (utils/make-directory! "./testing")

  (parse-template {:template "hello my name is {$name$} and I am {$age$} years old"
                   :values {:name "Keagan"
                            :age 23}})
  ())
