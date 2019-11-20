(ns account-service.saving-account-test
  (:require [midje.sweet :refer [facts
                                 fact
                                 =>
                                 against-background
                                 before]]
            [account-service.db.saving-account :refer [register!
                                                       accounts!
                                                       reset-records!
                                                       account-by-id!
                                                       account-by-customer-id!]]
            [account-service.auxiliary :refer [account-id
                                               account-st
                                               account-nd
                                               account-rd
                                               customer-id
                                               other-customer-id]]))

(facts "Store an account in an atom" :unit

       (against-background
        [(before :facts (reset-records!))]

        (fact "initial count of accounts is 0"
              (count (accounts!)) => 0)

        (fact "this account record is the first record and the accounts count is 1"
              (register! account-st account-id)
              => (merge account-st {:account-id account-id})

              (count (accounts!)) => 1)))

(facts "Get an account by ID" :unit

       (against-background
        [(before :facts (reset-records!))]

        (fact "get only the account fetched by ID between the three account records"
              (register! account-st account-id)
              (register! account-nd)
              (register! account-rd)

              (account-by-id! account-id)
              => (merge account-st {:account-id account-id}))

        (fact "get last account fetched by ID between both account records with the same ID"
              (register! account-st account-id)
              (register! account-nd account-id)

              (account-by-id! account-id)
              => (merge account-nd {:account-id account-id}))

        (fact "get no account fetched by ID between two account records with different IDs"
              (register! account-st)
              (register! account-nd)

              (account-by-id! account-id) => {})))

(facts "Get an account by customer ID" :unit

       (against-background
        [(before :facts (reset-records!))]

        (fact "get only the account fetched by customer ID between the three account records"
                (register! account-st account-id)
                (register! account-nd)
                (register! account-rd)

                (account-by-customer-id! customer-id)
                => (merge account-st {:account-id account-id}))

        (fact "get last account fetched by customer ID between both account records with the same customer ID"
              (register! account-st)
              (register! account-st account-id)

              (account-by-customer-id! customer-id)
              => (merge account-st {:account-id account-id}))

        (fact "get no account fetched by customer ID between two account records with different customer IDs"
              (register! account-st)
              (register! account-nd)

              (account-by-customer-id! other-customer-id) => {})))
