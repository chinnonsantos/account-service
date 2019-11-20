(ns account-service.service
  (:require [compojure.core :refer [defroutes
                                    GET
                                    POST]]
            [compojure.route :as route]
            [account-service.components.service :refer [home-page
                                                         list-accounts!
                                                         get-account!
                                                         customer->get-account!
                                                         create-account!
                                                         not-found]]
            [ring.middleware.defaults :refer [wrap-defaults
                                              api-defaults]]
            [ring.middleware.json :refer [wrap-json-body]]))

(defroutes app-routes
  (GET "/" [] (home-page))
  (GET "/account/" [] (list-accounts!))
  (GET "/account/:account-id/" request (get-account! request))
  (GET "/account/from-customer/:customer-id/" request
    (customer->get-account! request))
  (POST "/account/" request (create-account! request))
  (route/not-found (not-found)))

(def app
  (-> (wrap-defaults app-routes api-defaults)
      (wrap-json-body {:keywords? true :bigdecimals? true})))
