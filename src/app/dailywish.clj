(ns app.dailywish
  (:require [app.db :refer [db-spec]]
           [cheshire.core :as json]
           [clojure.java.jdbc :as jdbc]
           [ring.util.response :as response]))



(defn get-daily-wish-data-handler [request]
  ;; 从数据库获取最新一条
  (let [wish-date (jdbc/query db-spec
                              ["SELECT * FROM DailyWishCardData ORDER BY id DESC LIMIT 1"])]
    ;(println wish-date)
    (if (empty? wish-date)
      (response/response (json/encode {:message "没有找到心愿"}))
      (response/response (json/encode (first wish-date))))))