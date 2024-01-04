(ns app.server
  (:require [buddy.hashers :as hashers]
            [cheshire.core :as json]
            [clojure.java.jdbc :as jdbc]
            [clojure.string :as str]
            ;[compojure.core :refer [POST defroutes]]
            [compojure.core :refer [GET POST defroutes]]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-body]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.util.response :as response]
            [ring.middleware.cors :refer [wrap-cors]]
            [app.auth :as auth]
            [app.routes :as route]
            )
  (:import java.util.UUID))

;;在 Clojure 中，当您想要使用 Java 类时，应该使用 :import 而不是 :require 或 :use。:require 和 :use 是用于 Clojure 命名空间的，而 :import 是用于 Java 类的。所以，您应该将 (:import [java.util.UUID :as UUID]) 改为 (:import [java.util UUID])。


(def app
  (-> route/app-routes
      (wrap-json-body)
      (wrap-params)
      (wrap-cors :allow-origin ["*"] ;允许所有ip访问
                 :allow-methods [:get :post :put :delete :options] ;指定允许的HTTP请求方法，[:get :post :put :delete :options]
                 :allow-headers ["Content-Type" "Authorization"];指定在实际请求中可以使用哪些HTTP头
                 :max-age 3600 ;表示预检请求的结果（即对特定资源的CORS配置）可以被缓存多长时间（以秒为单位）。3600表示预检请求的结果可以被缓存一个小时。
                 :credentials true)));当设置为true时，表示响应可以暴露给前端JavaScript代码，即使请求是来自不同源的。这通常用于控制是否应该发送cookies或HTTP认证信息。
(defn -main []
  (jetty/run-jetty app {:port 3000}))


