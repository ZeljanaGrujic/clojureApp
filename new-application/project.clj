(defproject new-application "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [compojure "1.7.0"]
                 [ring/ring-jetty-adapter "1.9.6"]
                 [hiccup "1.0.5"]
                 [org.clojure/clojure-contrib "1.2.0"]   ;; for clojure.contrib.sql
                 [org.clojure/java.jdbc "0.7.12"]         ;; jdbc
                 [mysql/mysql-connector-java "8.0.30"]
                 [ring/ring-anti-forgery "1.3.0"];returns the HTML for the anti-forgery field
                 [ring/ring-defaults "0.3.4"]
                 [ring/ring-devel "1.8.0"]]
  :main new-application.server-config
  :target-path "target/%s"
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler new-application.core/app-handler}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
