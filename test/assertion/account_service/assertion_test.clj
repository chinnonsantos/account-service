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

       ; `setup` and `teardown`
       (against-background [(before :facts (start-server!))
                            (after :facts (stop-server!))]

                           (fact "Initial balance is 0"
                                 (response "/balance") => "0")))