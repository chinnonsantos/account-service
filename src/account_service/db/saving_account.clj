(ns account-service.db.saving-account)

(def records
  (atom []))

(defn accounts!
  "List all accounts"
  []
  @records)

(defn reset-records!
  "Remove all accounts"
  []
  (reset! records []))

(defn register!
  "Save the account data to an Atom and return the last record.
   Optionally an ID can be entered to override the runtime
   generated ID (useful for automated testing)."
  [account & [uuid-default]]
  (let [account-id (or uuid-default (java.util.UUID/randomUUID))]
    (->> (merge account {:account-id (str account-id)})
         (swap! records conj)
         (last))))

(defn account-by-id!
  "Get an account by ID"
  [account-id]
  (->> (filter #(= account-id (:account-id %)) (accounts!))
       (last)
       (conj {})))

(defn account-by-customer-id!
  "Get an account by customer ID"
  [customer-id]
  (->> (filter #(= customer-id (:customer-id %)) (accounts!))
       (last)
       (conj {})))
