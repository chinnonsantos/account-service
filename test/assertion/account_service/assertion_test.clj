(ns account-service.assertion-test
  (:require [midje.sweet :refer [facts
                                 fact
                                 =>
                                 against-background
                                 before
                                 after]]
            [account-service.auxiliary :refer [start-server!
                                               stop-server!
                                               response]]
            [cheshire.core :as json]))

(facts "Starting server, hitting some endpoints,
checking responses and stopping server" :assertion ;; filter label

       (against-background [(before :facts (start-server!)) ;; `setup`
                            (after :facts (stop-server!))] ;; `teardown`

                           (fact "Initial accounts list is []"
                                 (json/parse-string (response "/account/") true)
                                 => {:list []})

                           (fact "Initial account info id :account-id is []"
                                 (json/parse-string (response "/account/:account-id/") true)
                                 => {:account []})

                           (fact "Initial account info by customer id :customer-id is []"
                                 (json/parse-string (response "/account/from-customer/:customer-id/") true)
                                 => {:account []})))