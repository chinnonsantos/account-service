(ns account-service.assertion-test
  (:require [midje.sweet :refer [facts
                                 fact
                                 =>
                                 against-background
                                 before
                                 after]]
            [account-service.db.saving-account :refer [reset-records!]]
            [account-service.auxiliary :refer [start-server!
                                               stop-server!
                                               response
                                               endpoint
                                               account-id
                                               customer-id
                                               account-st
                                               account-st-json
                                               account-nd
                                               account-rd
                                               content-like-json
                                               rm-id-from-json
                                               rm-id]]
            [cheshire.core :as json]
            [clj-http.client :as http]))

(facts "Starting server, hitting some endpoints, checking responses and stopping server" :assertion ;; filter label

       (against-background
        [(before :facts [(reset-records!) (start-server!)]) ;; `setup`
         (after :facts (stop-server!))] ;; `teardown`

        (fact "initial accounts list is an empty list"
              (-> (response "/account/")
                  (json/parse-string true)) => '())

        (fact "initial account info is an empty object"
              (-> (str "/account/" account-id "/")
                  (response)
                  (json/parse-string  true)) => {})

        (fact "initial account info by customer ID is an empty list"
              (-> (str "/account/from-customer/" customer-id "/")
                  (response)
                  (json/parse-string true)) => {})

        (fact "check response body after register an account"
              (let [response (http/post (endpoint "/account/")
                                        (content-like-json account-st))]
                (-> (:body response)
                    (rm-id-from-json)) => account-st-json))

        (fact "account list count check when registering only one account, should list only one"

              (http/post (endpoint "/account/")
                         (content-like-json account-st))

              (let [customer-list (-> (response "/account/")
                                      (json/parse-string true))]

                (map rm-id customer-list) => (list account-st)))

        (fact "account list count check when registering two account, should list only two"

              (http/post (endpoint "/account/")
                         (content-like-json account-st))

              (http/post (endpoint "/account/")
                         (content-like-json account-nd))

              (let [customer-list (-> (response "/account/")
                                      (json/parse-string true))]

                (map rm-id customer-list) => (list account-st
                                                   account-nd)))

        (fact "account list count check when registering three account, should list only three"

              (http/post (endpoint "/account/")
                         (content-like-json account-st))

              (http/post (endpoint "/account/")
                         (content-like-json account-nd))

              (http/post (endpoint "/account/")
                         (content-like-json account-rd))

              (let [customer-list (-> (response "/account/")
                                      (json/parse-string true))]

                (map rm-id customer-list) => (list account-st
                                                   account-nd
                                                   account-rd)))

        (fact "account list count check when registering only one account with invalid data, should list none"

              (http/post (endpoint "/account/")
                         (content-like-json {:name "Uai!"}))

              (let [customer-list (-> (response "/account/")
                                      (json/parse-string true))]

                (map rm-id customer-list) => '()))))

(facts "Hitting account register route, with invalid account data, checking response status" :assertion

       (against-background
        [(before :facts [(reset-records!) (start-server!)])
         (after :facts (stop-server!))]

        (let [expected-status 422] ;; 422 Unprocessable Entity
          (fact "reject a transaction without customer ID"
                (let [response (http/post (endpoint "/account/")
                                          (content-like-json (dissoc account-st
                                                                     :customer-id)))]
                  (:status response) => expected-status))

          (fact "reject a transaction without bank branch"
                (let [response (http/post (endpoint "/account/")
                                          (content-like-json (dissoc account-st
                                                                     :bank-branch)))]
                  (:status response) => expected-status))

          (fact "reject a transaction without bank account"
                (let [response (http/post (endpoint "/account/")
                                          (content-like-json (dissoc account-st
                                                                     :bank-account)))]
                  (:status response) => expected-status))

          (fact "reject a transaction without limit"
                (let [response (http/post (endpoint "/account/")
                                          (content-like-json (dissoc account-st
                                                                     :limit)))]
                  (:status response) => expected-status)))))
