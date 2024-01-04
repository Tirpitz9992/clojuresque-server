(ns app.message
  (:require [app.db :refer [db-spec]]
            [clojure.java.jdbc :as jdbc]
            [cheshire.core :as json])
  (:import (java.time Instant))
  (:import java.sql.Timestamp))

;; im的服务器部分，暂时只写了存储部分
(defn insert-message [db-spec sender message timestamp]
  (println "Sender: "sender "Message: " message)
  (jdbc/with-db-transaction [chat_records db-spec]
                            (jdbc/insert! chat_records :chat_records {:username sender :message message :timestamp (Timestamp/from timestamp)})))


(defn send-message-handler [req]
  (let [body (:body req)
        message (get body "message")
        sender (get body "sender")
        timestamp (Instant/now)]
    (println "Request body:" body)
    (println "Sender:" sender "Message:" message)
    (insert-message db-spec sender message timestamp)
    {:status 200 :headers {"Content-Type" "application/json"} :body (json/generate-string {:message "Message sent successfully."})}))









;(println (jdbc/query db-spec ["SELECT * FROM chat_records"]))

;创建表
(defn create-chat-table []
  (jdbc/with-db-transaction [t-con db-spec]
                            (jdbc/execute! t-con ["CREATE TABLE IF NOT EXISTS chat_records (
                            id SERIAL PRIMARY KEY,
                            username VARCHAR(255),
                            message TEXT,
                            timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                          )"])))

