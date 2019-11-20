(ns account-service.saving-account-test
  (:require [midje.sweet :refer [facts
                                 fact
                                 =>
                                 against-background
                                 before]]
            [account-service.db.saving-account :refer [register!
                                                       accounts!
                                                       reset-records!]]
            [account-service.auxiliary :refer [account-st
                                               account-id]]))

(facts "Store an account in an atom" :unit

       (against-background
        [(before :facts (reset-records!))]

        (fact "initial count of accounts is 0"
              (count (accounts!)) => 0)

        (fact "this account record is the first record and the accounts count is 1"
              (register! account-st account-id)
              => (merge account-st {:account-id account-id})

              (count (accounts!)) => 1)))
