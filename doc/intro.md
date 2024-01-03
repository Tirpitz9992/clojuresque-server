# Introduction to test_clojure

TODO: write [great documentation](http://jacobian.org/writing/what-to-write/)

接下来是我的一些笔记，这是一个由Clojure编写的服务器，使用java.jdbc来访问数据库，数据库使用的是postgresql，而服务器主要使用ring


```clojure
(defn hash-password-for-user [user-id new-plain-password]
(let [hashed-password (hashers/derive new-plain-password)]
(jdbc/update! db-spec :users
{:password hashed-password}
["id=?" user-id])))

;;为特定用户hash密码
```
```clojure
(defn add-dailywishcard-data [imageuri wishtext contenttext]
  ;;添加每日心愿签数据
  (jdbc/with-db-connection [con db-spec]
  (jdbc/insert! con :DailyWishCardData {:imageuri imageuri :wishtext wishtext :contenttext contenttext})))

```

```clojure
(def app
  (-> app-routes
      (wrap-json-body)
      (wrap-params)
      (wrap-cors :allow-origin ["*"] ;允许所有ip访问
                 :allow-methods [:get :post :put :delete :options] ;指定允许的HTTP请求方法，[:get :post :put :delete :options]
                 :allow-headers ["Content-Type" "Authorization"];指定在实际请求中可以使用哪些HTTP头
                 :max-age 3600 ;表示预检请求的结果（即对特定资源的CORS配置）可以被缓存多长时间（以秒为单位）。3600表示预检请求的结果可以被缓存一个小时。
                 :credentials true)));当设置为true时，表示响应可以暴露给前端JavaScript代码，即使请求是来自不同源的。这通常用于控制是否应该发送cookies或HTTP认证信息。
```