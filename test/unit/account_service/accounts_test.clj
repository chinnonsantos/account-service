(ns account-service.accounts-test
  (:require [midje.sweet :refer [facts
                                 fact
                                 =>]]
            [account-service.components.accounts :refer [valid?]]
            [account-service.auxiliary :refer [account-st]]))

(facts "Income data integrity check" :unit

       (fact "an account without customer ID is not valid"
             (valid? (dissoc account-st :customer-id)) => false)

       (fact "an account without bank branch is not valid"
             (valid? (dissoc account-st :bank-branch)) => false)

       (fact "an account without bank account is not valid"
             (valid? (dissoc account-st :bank-account)) => false)

       (fact "an account without limit is not valid"
             (valid? (dissoc account-st :limit)) => false)

       (fact "an account with negative limit is not valid"
             (valid? (merge account-st {:limit -100})) => false)

       (fact "an account with a non-numeric limit is not valid"
             (valid? (merge account-st {:limit "a hundred"})) => false)

       (fact "an account with a customer ID, a bank branch, an bank account and a positive limit is valid"
             (valid? account-st) => true))
