(ns account-service.assertion-test
  (:require [midje.sweet :refer [facts
                                 fact
                                 =>
                                 against-background
                                 before
                                 after]]
            [account-service.auxiliary :refer [start-server!
                                               stop-server!
                                               response]]))

(facts "Starting server, hitting some endpoints,
checking responses and stopping server" :assertion ;; filter label

       (against-background [(before :facts (start-server!)) ;; `setup`
                            (after :facts (stop-server!))] ;; `teardown`

                           (fact "Initial accounts list is []"
                                 (response "/account/") => "[]")

                           (fact "Initial account info id 123 is []"
                                 (response "/account/123/") => "[]")

                           (fact "Initial account info by customer id 321 is []"
                                 (response "/account/from-customer/321/") => "[]")))