(ns account-service.service-test
  (:require [midje.sweet :refer [facts
                                 fact
                                 =>
                                 against-background]]
            [account-service.db.saving-account :as db]
            [account-service.auxiliary :refer [account-st
                                               account-st-json]]
            [ring.mock.request :as mock]
            [account-service.service :refer [app]]
            [cheshire.core :as json]))

(facts "Hitting main route, checking microservice health" :unit ;; filter label

       (against-background (json/generate-string {:message "Alive!"})
                           => "{\"message\":\"Alive!\"}") ;; mock Cheshire

       (let [response (app (mock/request :get "/"))] ;; mock Ring

         (fact "the header content-type is 'application/json'"
               (get-in response [:headers "Content-Type"])
               => "application/json; charset=utf-8")

         (fact "status response is 200"
               (:status response) => 200)

         (fact "body response is a JSON, being key is ':message' and value is 'Alive!'"
               (:body response) => "{\"message\":\"Alive!\"}")))

(facts "Hitting accounts list route, checking response" :unit

       (against-background (json/generate-string {:list []})
                           => "{\"list\":[]}")

       (let [response (app (mock/request :get "/account/"))]

         (fact "the header content-type is 'application/json'"
               (get-in response [:headers "Content-Type"])
               => "application/json; charset=utf-8")

         (fact "status response is 200"
               (:status response) => 200)

         (fact "body response is a JSON, being key is :list and value is []"
               (:body response) => "{\"list\":[]}")))

(facts "Hitting account info route, by account id, checking response" :unit

       (against-background (json/generate-string {:account []})
                           => "{\"account\":[]}")

       (let [response (app (mock/request :get "/account/:account-id/"))]

         (fact "the header content-type is 'application/json'"
               (get-in response [:headers "Content-Type"])
               => "application/json; charset=utf-8")

         (fact "status response is 200"
               (:status response) => 200)

         (fact "body response is a JSON, being key is :account and value is []"
               (:body response) => "{\"account\":[]}")))

(facts "Hitting account info route, by customer id, checking response" :unit

       (against-background (json/generate-string {:account []})
                           => "{\"account\":[]}")

       (let [response (app (mock/request :get "/account/from-customer/:customer-id/"))]

         (fact "the header content-type is 'application/json'"
               (get-in response [:headers "Content-Type"])
               => "application/json; charset=utf-8")

         (fact "status response is 200"
               (:status response) => 200)

         (fact "body response is a JSON, being key is :account and value is []"
               (:body response) => "{\"account\":[]}")))

(facts "Hitting account registration route with account test data, checking response" :unit

       (against-background (db/register! account-st) => account-st)

       (let [response (app (-> (mock/request :post "/account/")
                               (mock/json-body account-st)))]

         (fact "the header content-type is 'application/json'"
               (get-in response [:headers "Content-Type"])
               => "application/json; charset=utf-8")

         (fact "status response is 201"
               (:status response) => 201)

         (fact "body response is a JSON, with the same content that was submitted")
         (:body response) => account-st-json))

(facts "Hitting invalid route, checking routes not found" :unit

       (against-background (json/generate-string {:message "Not Found"})
                           => "{\"message\":\"Not Found\"}")

       (let [response (app (mock/request :get "/invalid/"))]

         (fact "the header content-type is 'application/json'"
               (get-in response [:headers "Content-Type"])
               => "application/json; charset=utf-8")

         (fact "status response is 404"
               (:status response) => 404)

         (fact "body response is a JSON, being key is :message and value is 'Not Found'"
               (:body response) => "{\"message\":\"Not Found\"}")))
