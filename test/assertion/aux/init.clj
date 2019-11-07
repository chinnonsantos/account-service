(ns aux.init
  (:require [account-service.handler :refer [app]] ;; available routes
            [ring.adapter.jetty :refer [run-jetty]]))

(def server (atom nil))

(defn start-server! [port]
  (swap! server
         (fn [_] (run-jetty app {:port port :join? false}))))

(defn stop-server! []
  (.stop @server))
