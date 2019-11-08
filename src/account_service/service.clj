(ns account-service.service
  (:require [compojure.core :refer [defroutes
                                    GET]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults
                                              site-defaults]]))

(defroutes app-routes
  (GET "/" [] "Alive!")
  (GET "/account/" [] "[]")
  (GET "/account/:account-id/" [] "[]")
  (GET "/account/from-customer/:customer-id/" [] "[]")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
