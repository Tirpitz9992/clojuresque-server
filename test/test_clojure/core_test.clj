(ns test-clojure.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [test-clojure.core :refer []]
            [clojure.java.jdbc :as jdbc]))


(def db-spec
  {:dbtype "postgresql"
   :dbname "postgres"
   :host "localhost"
   :port "5432"
   :user "postgres"
   :password "123"
   })

(defn create-user-table []
  (jdbc/execute! db-spec ["CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, name VARCHAR(255))"]))

(defn get-all-users []
  (jdbc/query db-spec ["SELECT * FROM users"]))

(create-user-table)


(get-all-users)