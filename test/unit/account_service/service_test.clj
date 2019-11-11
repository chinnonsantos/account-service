(ns account-service.service-test
  (:require [midje.sweet :refer [facts
                                 fact
                                 =>
                                 against-background]]
            [ring.mock.request :as mock]
            [account-service.service :refer [app]]
            [cheshire.core :as json]))

(facts "Hitting main route, check microservice health" :unit ;; filter label

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

(facts "Hitting accounts list route, check value" :unit

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

(facts "Hitting account info route, check value" :unit

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

(facts "Hitting account info route by customer id, check value" :unit

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

(facts "Hitting invalid route, check routes not found" :unit

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
