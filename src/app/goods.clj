(ns app.goods
  (:require [app.db :refer [db-spec]]
            [clojure.java.jdbc :as jdbc]
            [clojure.string :as str]
            [ring.util.response :as response]
            [cheshire.core :as json]))


;;创建goods表
(defn create-goods-db []
  (jdbc/with-db-connection [conn db-spec]
  (jdbc/execute! conn
                 ["CREATE TABLE goods (
                      id SERIAL PRIMARY KEY,
                      image_url TEXT,
                      title VARCHAR(255),
                      description TEXT,
                      price NUMERIC
                    )"])))
(defn get-all-goods []
  (let [goods (jdbc/with-db-connection [conn db-spec]
                                       (jdbc/query conn ["SELECT * FROM goods"]))]
    ;(println goods)
    (json/generate-string goods)))

(defn add-good [good]
  ;; 使用jdbc的insert!函数向表中插入记录
  (jdbc/with-db-connection [conn db-spec]
                           (jdbc/insert! conn :goods good)))

;; 实例
(def new-good {:image_url "https://images.pexels.com/photos/39811/pexels-photo-39811.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
               :title "商品测试3"
               :description "这个是为了测试商品页面的"
               :price 99.99})





