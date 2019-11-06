(defproject account-service "1.0.0" ;; Semantic Versioning 2.0.0
  :description "Account microservices demo"
  :url "https://github.com/chinnonsantos/account-service"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler account-service.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]
                        [midje "1.9.9"]]
         :plugins [[lein-midje "3.2.1"]
                   [lein-cloverage "1.1.2"]]}})
