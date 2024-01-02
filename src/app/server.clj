(ns app.server
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]
            [clojure.string :as str]
            [ring.middleware.params :refer [wrap-params]]
            [clojure.java.jdbc :as jdbc]
            [buddy.hashers :as hashers]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route])
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
  (let [user (jdbc/query db-spec
                         ["SELECT * FROM users WHERE username = ?" username] ;;查找数据库结构
                         {:row-fn first})] ;;
    (if user
      (if (hashers/check password (:password user)) ;; 使用hasher库验证
        {:username (:username user)
         :nickname (:name user) ; 昵称
         :avatar_url (:avatar_url user)}
        false)
      false)))


(defn login-handler [request]
  (let [params (:params request)
        username (str/trim (get params "username"))
        password (str/trim (get params "password"))
        user (verify-user username password)]
    (println "Debug 登录信息: " "Username:" username "Password:" password) ;; 打印调试信息
    (if user
      (let [session-id (generate-session-id)] ;; 生成sessionID
        (response/response {:message "Login successful"
                            :nickname (:nickname user)
                            :avatar_url (:avatar_url user)
                            :session_id session-id})) ;; 将sessionID发送给客户端
      (-> (response/response {:message "Invalid username or password"})
          (assoc :status 401)))))



(defroutes app-routes
           (POST "/login" [username password] ;; 创建路由 /login
             (login-handler {:params {:username username :password password}}))
           (route/not-found "Not Found"))

(defn -main []
  (jetty/run-jetty (wrap-params app-routes) {:port 3000}))
