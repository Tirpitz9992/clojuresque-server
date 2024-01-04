# 笔记

接下来是我的一些笔记，这是一个由Clojure编写的服务器，使用java.jdbc来访问数据库，数据库使用的是postgresql，而服务器主要使用ring
## 关于Ring库
Clojure Ring 是一个用于构建 web 应用的 Clojure 库，它遵循简单的设计原则，提供了一个用于处理 HTTP 请求和响应的抽象层。Ring 通过中间件和适配器的概念，使得开发者能够轻松地扩展和定制其功能。
Ring 的核心是一个处理 HTTP 请求的函数，这个函数接收一个请求 map 并返回一个响应 map。请求 map 包含了所有的 HTTP 请求信息，如 URI、请求头和请求体。响应 map 则包含了返回给客户端的信息，如状态码、响应头和响应体。
Ring 中间件是一种特殊的函数，它接收一个 Ring 处理函数并返回一个新的处理函数。这个新的处理函数可以在调用原始处理函数之前或之后执行一些操作，从而实现对请求或响应的修改。中间件可以被组合起来，形成一个处理链，每个中间件都可以添加额外的功能，如会话管理、身份验证和日志记录。
Ring 适配器则是连接 Ring 和各种 web 服务器的桥梁，如 Jetty 或 Tomcat。适配器负责将服务器的请求转换为 Ring 可以理解的请求 map，并将 Ring 的响应 map 转换回服务器理解的格式。
Ring 的设计哲学是简单和可组合性，它鼓励开发者通过组合小的、专注的中间件来构建复杂的应用程序。Ring 也非常灵活，允许开发者根据需要轻松地引入新的中间件或替换现有的中间件。
Ring 库的使用广泛，它是许多 Clojure web 框架和库的基础，如 Compojure（一个路由库），以及更高级的框架，如 Luminus。通过使用 Ring，Clojure 开发者可以利用 Clojure 的强大功能和简洁的语法来构建高效、可维护的 web 应用程序。

## ring库的基本api

Reading webpages...

__ff__source[{"url":"https://clojure.github.io/clojure/","title":"Overview - Clojure v1.11 API documentation","favicon":"https://www.google.com/s2/favicons?sz=64&domain_url=https://clojure.github.io/clojure/"},{"url":"http://practical.li/blog/posts/consuming-apis-with-clojure/","title":"Practicalli: Consuming APIs from Clojure","favicon":"https://www.google.com/s2/favicons?sz=64&domain_url=http://practical.li/blog/posts/consuming-apis-with-clojure/"},{"url":"https://santhoshkris.medium.com/rest-apis-with-clojure-part-i-working-with-the-apis-1b045e00114c","title":"","favicon":"https://www.google.com/s2/favicons?sz=64&domain_url=https://santhoshkris.medium.com/rest-apis-with-clojure-part-i-working-with-the-apis-1b045e00114c"},{"url":"https://www.reddit.com/r/Clojure/comments/b3nokc/what_is_the_most_popular_clojure_rest_framework/","title":"Blocked","favicon":"https://www.google.com/s2/favicons?sz=64&domain_url=https://www.reddit.com/r/Clojure/comments/b3nokc/what_is_the_most_popular_clojure_rest_framework/"},{"url":"https://clojure.org/api/api","title":"Clojure - API Documentation","favicon":"https://www.google.com/s2/favicons?sz=64&domain_url=https://clojure.org/api/api"},{"url":"https://blog.redplanetlabs.com/2023/10/11/introducing-ramas-clojure-api/","title":"Introducing Rama’s Clojure API: build end-to-end scalable backends in 100x less code – Blog","favicon":"https://www.google.com/s2/favicons?sz=64&domain_url=https://blog.redplanetlabs.com/2023/10/11/introducing-ramas-clojure-api/"},{"url":"https://clojure.org/cheatsheet","title":"Clojure - Cheatsheet","favicon":"https://www.google.com/s2/favicons?sz=64&domain_url=https://clojure.org/cheatsheet"},{"url":"https://auth0.com/blog/create-an-api-server-with-clojure-and-pedestal/","title":"Create an API server with Clojure and Pedestal","favicon":"https://www.google.com/s2/favicons?sz=64&domain_url=https://auth0.com/blog/create-an-api-server-with-clojure-and-pedestal/"},{"url":"https://www.youtube.com/watch?v=JWa4NhjWNHQ","title":"Building a RESTful Web API in Clojure - a new approach - Malcolm Sparks - YouTube","favicon":"https://www.google.com/s2/favicons?sz=64&domain_url=https://www.youtube.com/watch?v=JWa4NhjWNHQ"}]__endsourceClojure Ring 是一个用于构建 web 应用和服务的 Clojure 库。它提供了一个简单的、可扩展的 web 服务器和客户端库，使用中间件来处理请求和响应。Ring 的 API 遵循 HTTP 的语义，使得开发 web 应用变得直观和灵活。

以下是一些常用的 Ring API：

1. **ring.core** - 这是 Ring 的核心命名空间，提供了处理 HTTP 请求和响应的基本工具。例如，`ring.util.response` 提供了创建响应的函数，如 `response`、`redirect` 和 `not-found`。

2. **ring.middleware** - Ring 中间件是可以被插入到请求处理流程中的组件，用于修改请求或响应，或者执行其他任务。常见的中间件包括 `ring.middleware.session` 用于处理会话，`ring.middleware.keyword-params` 用于将参数转换为 Clojure 关键字，以及 `ring.middleware.multipart-params` 用于处理多部分请求。

3. **ring.adapter** - Ring 适配器用于连接 Ring 应用程序和各种 web 服务器。例如，`ring.adapter.jetty` 允许你使用 Jetty 服务器运行 Ring 应用程序。

4. **ring.util** - 这个命名空间提供了一些实用工具函数，如 `ring.util.codec` 用于编码和解码数据，`ring.util.http-status` 提供了 HTTP 状态码的映射。

5. **ring.handler** - 这个命名空间包含了一些用于处理特定类型请求的函数，例如静态文件处理器 `ring.handler.site`。

6. **ring.util.servlet** - 提供了将 Ring 应用程序转换为 Servlet，以便在支持 Servlet 的 web 服务器上运行的工具。

7. **ring.util.mime-type** - 用于根据文件扩展名确定 MIME 类型的实用函数。


## 关于我的一部分代码笔记
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