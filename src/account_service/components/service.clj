(ns account-service.components.service
  (:require [cheshire.core :as json]
            [account-service.db.saving-account :as db]
            [account-service.components.accounts :refer [valid?]]))

(defn header-json
  "Create a JSON response header"
  [data-map & [status]]
  {:status (or status 200)
   :headers {"Content-Type"
             "application/json; charset=utf-8"}
   :body (json/generate-string data-map)})

(defn get-account-id [request]
  (:account-id (:route-params request)))

(defn get-customer-id [request]
  (:customer-id (:route-params request)))

(defn home-page
  "Main route"
  []
  (header-json {:message "Alive!"}))

(defn list-accounts!
  "Accounts list route"
  []
  (-> (db/accounts!)
      (header-json)))

(defn get-account!
  "Account info route (account details)"
  [request]
  (-> (get-account-id request)
      (db/account-by-id!)
      (header-json)))

(defn customer->get-account!
  "Account info by customer route (account details)"
  [request]
  (-> (get-customer-id request)
      (db/account-by-customer-id!)
      (header-json)))

(defn create-account!
  "Account register route, create a new account"
  [request]
  (let [body (:body request)]
    (if (valid? body)
      (-> (db/register! body)
          (header-json 201))
      (header-json {:mensagem "Unprocessable Entity"} 422))))

(defn not-found
  "Response for any route that does not exist"
  []
  (header-json {:message "Not Found"}))
