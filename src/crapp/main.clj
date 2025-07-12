(ns crapp.main
  (:require [crapp.template :as template]
            [crapp.config :as conf]))

(defn prompt-template []
  (println "\nChoose your project template:")
  (let [templates (-> (conf/read-value :templates :list)
                      (keys)
                      (vec))]
    (doseq [[template index] (zipmap templates
                                     (->> templates
                                          (count)
                                          (inc)
                                          (range 1)))]
      (println (str index ". " (name template))))

    (print (str "Enter template number (1 - " (count templates) " | default 1): "))
    (flush)

    (let [n (or (parse-long (read-line)) 0)
          default (first templates)]
      (if (or (< n 1) (> n (count templates)))
        (do (println "\nUsing default...")
            default)
        (nth templates (dec n))))))

(defn prompt-component [component-group components]
  (println (str "\nChoose your " (name component-group) ":"))

  (doseq [[component index] (zipmap components
                                    (->> components
                                         (count)
                                         (inc)
                                         (range 1)))]
    (println (str index ". " (name component))))

  (print (str "Enter component number (1 - " (count components) " | default 1): "))
  (flush)

  (let [n (or (parse-long (read-line)) 0)
        default (first components)]
    (if (or (< n 1) (> n (count components)))
      (do (println (str "\nUsing default: " (name default) "..."))
          default)
      (nth components (dec n)))))

(defn -main []
  (println "Welcome to CRAPP!")
  (print "Project name: ")
  (flush)
  (let [project (read-line)
        template (prompt-template)
        component-groups (conf/read-value :templates
                                          :list
                                          template
                                          :component-groups)
        components (reduce (fn [acc [cg cl]]
                             (conj acc (prompt-component cg cl)))
                           [] component-groups)]
    (template/create-project! project template components)))

(comment
  (prompt-template)
  (prompt-component :router [:reitit :compojure])
  (-main)
  ())
