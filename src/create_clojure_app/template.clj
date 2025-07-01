(ns create-clojure-app.template
  (:require [clojure.string :as string]
            [create-clojure-app.utils :as utils]))

(defn parse-template [{:keys [template values]}]
  (string/join (map
                (fn [part]
                  (or (get values (keyword part)) part))
                (string/split template #"\$"))))

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
                         (utils/kebab-to-snake))]
        (utils/make-directory! (str targetpath "/" dirname))
        (deploy-template-files! (str basepath "/" filename)
                                (str targetpath "/" dirname)
                                values))

      (let [original (slurp (str basepath "/" filename))
            replaced (parse-template {:template original
                                               :values values})]
        (spit (str targetpath "/" filename) replaced)))))

(comment
  (let [dir (utils/list-files "./templates/default")]
    (mapv (fn [f]
            {:filename (utils/filename f)
             :is-directory (utils/is-directory f)}) dir))

  (deploy-template-files! "./templates/default" "./testing" {:project "my-app"})

  (->> "project"
       (assoc {:values {:project "my-app"}} :template)
       (parse-template)
       (utils/kebab-to-snake)
       (str "./templates/default" "/"))

  (utils/make-directory! "./testing")

  (parse-template {:template "hello my name is $name$ and I am $age$ years old"
                   :values {:name "Keagan"
                            :age 23}})
  ())
