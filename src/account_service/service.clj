(ns account-service.service
  (:require [compojure.core :refer [defroutes
                                    GET
                                    POST]]
            [compojure.route :as route]
            [account-service.db.saving-account :as db]
            [account-service.components.accounts :refer [valid?]]
            [ring.middleware.defaults :refer [wrap-defaults
                                              api-defaults]]
            [ring.middleware.json :refer [wrap-json-body]]
            [cheshire.core :as json]))

(defn header-json [data-map & [status]]
  {:status (or status 200)
   :headers {"Content-Type"
             "application/json; charset=utf-8"}
   :body (json/generate-string data-map)})

(defroutes app-routes
  (GET "/" []
    (header-json {:message "Alive!"}))
  (GET "/account/" []
    (header-json '()))
  (GET "/account/:account-id/" []
    (header-json {}))
  (GET "/account/from-customer/:customer-id/" []
    (header-json {}))
  (POST "/account/" request
    (if (valid? (:body request))
      (-> (db/register! (:body request))
          (header-json 201))
      (header-json {:mensagem "Unprocessable Entity"} 422)))
  (route/not-found (header-json {:message "Not Found"})))

(def app
  (-> (wrap-defaults app-routes api-defaults)
      (wrap-json-body {:keywords? true :bigdecimals? true})))
