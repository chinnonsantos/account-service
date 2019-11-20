(ns account-service.service-test
  (:require [midje.sweet :refer [facts
                                 fact
                                 =>
                                 against-background]]
            [account-service.db.saving-account :as db]
            [account-service.components.accounts :refer [valid?]]
            [account-service.service :refer [app]]
            [ring.mock.request :as mock]
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

       (against-background (json/generate-string '()) => "[]")

       (let [response (app (mock/request :get "/account/"))]

         (fact "the header content-type is 'application/json'"
               (get-in response [:headers "Content-Type"])
               => "application/json; charset=utf-8")

         (fact "status response is 200"
               (:status response) => 200)

         (fact "body response is a JSON, initial value should be []"
               (:body response) => "[]")))

(facts "Hitting account info route, by account id, checking response" :unit

       (against-background (json/generate-string {}) => "{}")

       (let [response (app (mock/request :get "/account/:account-id/"))]

         (fact "the header content-type is 'application/json'"
               (get-in response [:headers "Content-Type"])
               => "application/json; charset=utf-8")

         (fact "status response is 200"
               (:status response) => 200)

         (fact "body response is a JSON, initial value should be {}"
               (:body response) => "{}")))

(facts "Hitting account info route, by customer id, checking response" :unit

       (against-background (json/generate-string {}) => "{}")

       (let [response (app (mock/request :get "/account/from-customer/:customer-id/"))]

         (fact "the header content-type is 'application/json'"
               (get-in response [:headers "Content-Type"])
               => "application/json; charset=utf-8")

         (fact "status response is 200"
               (:status response) => 200)

         (fact "body response is a JSON, initial value should be {}"
               (:body response) => "{}")))

(facts "Hitting account registration route with account test data, checking response" :unit

       (against-background [(valid? {:limit 15000})
                            => true ;; Mock of `trans/valid?`
                            (db/register! {:limit 15000})
                            => {:limit 15000} ;; Mock of `db/register!`
                            ])

       (let [response (app (-> (mock/request :post "/account/") ;; Mock of `/account/` route
                               (mock/json-body {:limit 15000}) ;; Creating JSON for body POST
                               ))]

         (fact "the header content-type is 'application/json'"
               (get-in response [:headers "Content-Type"])
               => "application/json; charset=utf-8")

         (fact "status response is 201"
               (:status response) => 201)

         (fact "body response is a JSON, with the same content that was submitted"
               (:body response) => "{\"limit\":15000}")))

(facts "Hitting account register route with account test data, checking response" :unit

       (against-background [(valid? {:limit 15000})
                            => true ;; Mock of `trans/valid?`
                            (db/register! {:limit 15000})
                            => {:limit 15000} ;; Mock of `db/register!`
                            ])

       (let [response (app (-> (mock/request :post "/account/") ;; Mock of `/account/` route
                               (mock/json-body {:limit 15000}) ;; Creating JSON for body POST
                               ))]

         (fact "the header content-type is 'application/json'"
               (get-in response [:headers "Content-Type"])
               => "application/json; charset=utf-8")

         (fact "status response is 201"
               (:status response) => 201)

         (fact "body response is a JSON, with the same content that was submitted"
               (:body response) => "{\"limit\":15000}")))

(facts "Hitting customer register route with INVALID client test data, checking response" :unit

       (against-background [(valid? {:anotherkey "Any!"}) => false])

       (let [response (app (-> (mock/request :post "/account/")
                               (mock/json-body {:anotherkey "Any!"})))]

         (fact "the header content-type is 'application/json'"
               (get-in response [:headers "Content-Type"])
               => "application/json; charset=utf-8")

         (fact "status response is 422"
               (:status response) => 422)

         (fact "body response is a JSON, with the same content that was submitted"
               (:body response) => "{\"mensagem\":\"Unprocessable Entity\"}")))

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
