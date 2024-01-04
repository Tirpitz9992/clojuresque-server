(ns app.routes
  (:require [compojure.core :refer [GET POST defroutes]]
            [app.auth :as auth]
            [app.message :as message]
            [app.dailywish :as wish]
            [compojure.route :as route]
            )
  )

(defroutes app-routes
           (POST "/message" req ;; 路由im
             (message/send-message-handler req))
           (GET "/daily-wish" [] wish/get-daily-wish-data-handler) ;路由每日许愿
           (POST "/login" req ;; 创建路由 /login
             (auth/login-handler req))
           (route/not-found "Not Found"))
