(ns account-service.components.accounts)

; TODO: implement the checking of 'customer-id' value, if value is UUID?
(defn valid? [account]
  (let [limit (:limit account)]
    (and (contains? account :customer-id)
         (contains? account :bank-branch)
         (contains? account :bank-account)
         (contains? account :limit)
         (number? limit)
         (pos? limit))))
