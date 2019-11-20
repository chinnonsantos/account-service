(ns account-service.auxiliary
  (:require [account-service.service :refer [app]]
            [ring.adapter.jetty :refer [run-jetty]]
            [clj-http.client :as http]
            [cheshire.core :as json])
  (:import [java.util UUID]))

(def port 9000)

(def server (atom nil))

(defn start-server! []
  (swap! server
         (fn [_] (run-jetty app {:port port
                                 :join? false}))))

(defn stop-server! []
  (.stop @server))

(defn endpoint [route]
  (str "http://localhost:"
       port route))

(def request-http
  (comp http/get endpoint))

(defn response [route]
  (:body (request-http route)))

(defn content-like-json [transaction]
  {:content-type :json
   :body (json/generate-string transaction)
   :throw-exceptions false})

(defn generate-json [data]
  (json/generate-string data {:escape-non-ascii true}))

(def account-id (str (UUID/randomUUID)))

(def customer-id (str (UUID/randomUUID)))

(def other-customer-id (str (UUID/randomUUID)))

(def account-st
  {:customer-id customer-id
   :bank-branch "0001"
   :bank-account "1234567-8"
   :limit 15000})

(def account-st-json
  (generate-json account-st))

(def account-nd
  {:customer-id "03012004-3296-44aa-82ac-bede4f6b86f2"
   :bank-branch "0001"
   :bank-account "7654321-0"
   :limit 3400})

(def account-rd
  {:customer-id "89cf0f93-8a83-4869-87cf-483a40ca363c"
   :bank-branch "0001"
   :bank-account "2357111-3"
   :limit 800})

(defn rm-id
  "Remove the account-id key from set"
  [set]
  (dissoc set :account-id))

(defn rm-id-from-json
  "Remove the account-id key from JSON string"
  [json]
  (-> (json/parse-string json true)
      (rm-id)
      (json/generate-string {:escape-non-ascii true})))
