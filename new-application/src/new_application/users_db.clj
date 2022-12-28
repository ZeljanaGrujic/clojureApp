(ns new-application.users-db
  (:require
    [clojure.java.jdbc :as sql]                             ;sql/sqlite database
    [clojure.pprint :as p]))

(def sql-db {
             :classname   "org.sqlite.JDBC"
             :subprotocol "sqlite"
             :subname     "resources/users.db"
             })

;(sql/db-do-commands sql-db
;                    "DROP TABLE users")

;(sql/db-do-commands sql-db
;                    "CREATE TABLE users (
;                    id integer primary key autoincrement,
;                     owner_name varchar(255),
;                     owner_surname varchar(255),
;                     phone varchar(255),
;                     password varchar(255) );"
;                    )

;(p/print-table (sql/query sql-db ["SELECT * FROM users"]))


;;vrsicu proveru preko password i phone
(defn create-user [user]
  (sql/execute! sql-db ["INSERT INTO users (owner_name, owner_surname, phone, password) VALUES (?, ?, ?, ?) " (:owner_name user) (:owner_surname user) (:phone user) (:password user)]))
;(create-user {:owner_name "Biljana" :owner_surname "Grujic" :phone "0600133611" :password "2Fr4AA"})

(defn check-credentials [user]
  (sql/query sql-db ["SELECT * FROM users WHERE phone=? AND password=?" (:phone user) (:password user)]))
(check-credentials {:phone "0600133611" :password "2Fr4AA" }) ;ako ne nadje vraca () praznu listu, ako nadje vraca celog usera

(defn get-next-user-id []
  (+ 1 (:m (nth (sql/query sql-db ["SELECT MAX(id) as m FROM users"]) 0))))
(get-next-user-id)

(defn get-user-id-by-phone [phone]
  (:id (nth (sql/query sql-db ["SELECT id FROM users WHERE phone=?" phone]) 0)))
(get-user-id-by-phone "0600133611")