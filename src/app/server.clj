(ns app.server
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]
            [clojure.string :as str]
            [ring.middleware.params :refer [wrap-params]]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]))
(def db-spec
  {
   :dbtype "postgresql"
   :dbname "tiz"
   :host "localhost"
   :port "5432"
   :user "postgres"
   :password "123"
   })




(defn app-handler [request]
  (case (:request-method request)
    ;;:get (response/response "这是从服务器发送的字符串")
    :post (let [body (slurp (:body request))
                modified-body (str "字符串: " body)]
            (println "收到的消息：" body)
            (response/response modified-body))
    ))

(defn -main [& args]
  (jetty/run-jetty (wrap-params app-handler) {:port 3000}))
