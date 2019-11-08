(ns account-service.handler-test
  (:require [midje.sweet :refer [facts
                                 fact
                                 =>]]
            [ring.mock.request :as mock]
            [account-service.handler :refer [app]]))

(facts "Hitting main route, check microservice health" :unit ;; filter label

       (fact "status response is 200"
             (let [response (app (mock/request :get "/"))]
               (:status response) => 200))

       (fact "body response is 'Alive!'"
             (let [response (app (mock/request :get "/"))]
               (:body response) => "Alive!")))

(facts "Hitting accounts list route, check value" :unit ;; filter label

       (fact "status response is 200"
             (let [response (app (mock/request :get "/account/"))]
               (:status response) => 200))

       (fact "body response is 0"
             (let [response (app (mock/request :get "/account/"))]
               (:body response) => "[]")))

(facts "Hitting account info route, check value" :unit ;; filter label

       (fact "status response is 200"
             (let [response (app (mock/request :get "/account/:account-id/"))]
               (:status response) => 200))

       (fact "body response is 0"
             (let [response (app (mock/request :get "/account/:account-id/"))]
               (:body response) => "[]")))

(facts "Hitting account info route by customer id, check value" :unit ;; filter label

       (fact "status response is 200"
             (let [response (app (mock/request :get "/account/from-customer/:customer-id/"))]
               (:status response) => 200))

       (fact "body response is 0"
             (let [response (app (mock/request :get "/account/from-customer/:customer-id/"))]
               (:body response) => "[]")))

(facts "Hitting invalid route, check routes not found" :unit ;; filter label

       (fact "status response is 404"
             (let [response (app (mock/request :get "/invalid/"))]
               (:status response) => 404))

       (fact "body response is 'Not Found'"
             (let [response (app (mock/request :get "/invalid/"))]
               (:body response) => "Not Found")))