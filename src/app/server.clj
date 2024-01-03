(ns app.server
  (:require [buddy.hashers :as hashers]
            [cheshire.core :as json]
            [clojure.java.jdbc :as jdbc]
            [clojure.string :as str]
            [compojure.core :refer [POST defroutes]]
            [compojure.core :refer [GET POST defroutes]]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-body]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.util.response :as response]
            [ring.middleware.cors :refer [wrap-cors]]
            )
  (:import java.util.UUID))

;;在 Clojure 中，当您想要使用 Java 类时，应该使用 :import 而不是 :require 或 :use。:require 和 :use 是用于 Clojure 命名空间的，而 :import 是用于 Java 类的。所以，您应该将 (:import [java.util.UUID :as UUID]) 改为 (:import [java.util UUID])。

(def db-spec ;;数据库信息
  {:dbtype "postgresql"
   :dbname "postgres"
   :host "localhost"
   :port 5432
   :user "postgres"
   :password "123"})


(defn generate-session-id []
  (str ( UUID/randomUUID))) ;; 生成一个随机的UUID作为sessionID

(defn verify-user [username password]
  (let [result (jdbc/query db-spec
                         ["SELECT * FROM users WHERE username = ?" username] );;查找数据库结构
                         user (first result)] ;;
    (println user)
    (if user
      (if (hashers/verify password (:password user)) ;; 使用hasher库验证
        {:username (:username user)
         :nickname (:name user) ; 昵称
         :avatar_url (:avatar_url user)}
        false)
      false)))


(defn login-handler [request] ;;接受一个requst参数
  (let [params (:body request)
        raw-username (get params "username") ;;  检查用户名和密码是否都提供了
        raw-password (get params "password")]
    (if (and raw-username raw-password) ;; 如果都提供了，去除可能存在的前后空格
      (let [username (str/trim raw-username)
            password (str/trim raw-password)
            ;; 使用verify-user函数验证用户名和密码
            user (verify-user username password)]
        (if user
          (let [session-id (generate-session-id)]
            (-> (response/response (json/encode {:message "登录成功" ;; 创建一个包含登录成功信息的JSON响应
                                                 :nickname (:nickname user)
                                                 :avatar_url (:avatar_url user)
                                                 :session_id session-id}))
                ;; 设置响应的Content-Type为application/json,
                (response/content-type "application/json")))
          (-> (response/response (json/encode {:message "用户名和密码错误"}));; 如果用户验证失败，返回一个包含错误信息的JSON响应，并设置状态码为401
              (response/content-type "application/json")
              (assoc :status 401))))
      (-> (response/response (json/encode {:message "用户名和密码不能为空"}))
          (response/content-type "application/json")
          (assoc :status 400)))));; 如果用户名或密码未提供，返回一个错误信息的JSON响应，并设置状态码为400

(defn get-daily-wish-data-handler [request]
  ;; 从数据库获取最新一条
  (let [wish-date (jdbc/query db-spec
                              ["SELECT * FROM DailyWishCardData ORDER BY id DESC LIMIT 1"])]
    (println wish-date)
    (if (empty? wish-date)
      (response/response (json/encode {:message "没有找到心愿"}))
      (response/response (json/encode (first wish-date))))))

;(println (jdbc/query db-spec ["SELECT * FROM DailyWishCardData ORDER BY id DESC LIMIT 1"]))


(defroutes app-routes
           (GET "/daily-wish" [] get-daily-wish-data-handler) ;路由每日许愿
           (POST "/login" req ;; 创建路由 /login
             (login-handler req))
           (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-json-body)
      (wrap-params)
      (wrap-cors :allow-origin ["*"] ;允许所有ip访问
                 :allow-methods [:get :post :put :delete :options] ;指定允许的HTTP请求方法，[:get :post :put :delete :options]
                 :allow-headers ["Content-Type" "Authorization"];指定在实际请求中可以使用哪些HTTP头
                 :max-age 3600 ;表示预检请求的结果（即对特定资源的CORS配置）可以被缓存多长时间（以秒为单位）。3600表示预检请求的结果可以被缓存一个小时。
                 :credentials true)));当设置为true时，表示响应可以暴露给前端JavaScript代码，即使请求是来自不同源的。这通常用于控制是否应该发送cookies或HTTP认证信息。
(defn -main []
  (jetty/run-jetty app {:port 3000}))


