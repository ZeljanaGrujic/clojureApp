(ns new-application.server-config
  (:require
    [ring.adapter.jetty :as jetty]
    [compojure.handler :as handler]
    [projekat.core :as core]
    ))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
