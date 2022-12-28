(ns new-application.food-orders-db
  (:require
    [clojure.java.jdbc :as sql]                             ;sql/sqlite database
    [clojure.pprint :as p]))

(def sql-db {
             :classname   "org.sqlite.JDBC"
             :subprotocol "sqlite"
             :subname     "resources/food_orders.db"
             })

;(sql/db-do-commands sql-db
;                    "DROP TABLE food_orders")
;(sql/db-do-commands sql-db "DROP TABLE food_types")

;(sql/db-do-commands sql-db
;                    "CREATE TABLE food_orders (
;                    id integer primary key autoincrement,
;                     amount double default 1000,
;                     do_date varchar(255),
;                     month_name varchar(255),
;                     type_id integer );"
;                    )
;
;(sql/db-do-commands sql-db
;                    "CREATE TABLE food_types (
;                    id integer primary key autoincrement,
;                     type_name varchar(255),
;                     price double );"
;                    )

(defn create-food-type [food-type]
  (sql/execute! sql-db ["INSERT INTO food_types (type_name, price) VALUES (?, ?) "  (:type_name food-type) (:price food-type)]))

;(create-food-type {:type_name "Pantelic zito" :price 500})
;(create-food-type {:type_name "Pantelic vitamini" :price 200})

(p/print-table (sql/query sql-db ["SELECT * FROM food_types"]))

(defn get-id-by-type-name [type_name]
  (:id (nth (sql/query sql-db ["SELECT id FROM food_types WHERE type_name=?"  type_name]) 0)))
(get-id-by-type-name "Pantelic zito")
(get-id-by-type-name "Pantelic vitamini")


(defn new-food-order [{id :id amount :amount do_date :do_date month_name :month_name type_name :type_name}]
  (sql/execute! sql-db ["INSERT INTO food_orders (do_date, month_name, type_id) VALUES (?, ?, ?) " do_date month_name (get-id-by-type-name type_name)]))

;(new-food-order {:do_date "1.2.2023" :month_name "Februar" :type_name "Pantelic zito"})

(p/print-table (sql/query sql-db ["SELECT * FROM food_orders"]))

(defn get-next-food-id []
  (+ 1 (:m (nth (sql/query sql-db ["SELECT MAX(id) as m FROM food_orders"]) 0))))
(get-next-food-id)

;;VIDETI KAKO CU URADITI OVE JOINOVE
(defn list-full-forders [month_name]
  (sql/query sql-db ["SELECT food_orders.id, food_orders.amount, food_orders.do_date, food_orders.month_name, food_types.type_name FROM food_orders JOIN food_types ON food_orders.type_id =food_types.id WHERE month_name= ?"month_name]))

(list-full-forders "Januar")

(defn list-type-names []
  (sql/query sql-db ["SELECT DISTINCT type_name FROM food_types"]))
(list-type-names)

(defn list-full-forders-by-name [type_name]
  (sql/query sql-db ["SELECT food_orders.id, food_orders.amount, food_orders.do_date, food_orders.month_name FROM food_orders JOIN food_types ON food_orders.type_id =food_types.id WHERE food_types.type_name= ?"type_name]))


(defn list-full-forders-delete []
  (sql/query sql-db ["SELECT food_orders.id, food_orders.amount, food_orders.do_date, food_orders.month_name, food_types.type_name FROM food_orders JOIN food_types ON food_orders.type_id =food_types.id"]))

(list-full-forders-delete)

;get-food-order-by-id
(defn get-food-order-by-id [id]
  (nth (filter #(= (:id %) id) (sql/query sql-db ["SELECT * FROM food_orders"])) 0))
(get-food-order-by-id 1)

(defn delete-food-order [forder]
  (sql/execute! sql-db ["DELETE FROM food_orders WHERE id = ?"(:id forder)]))


;;;;;RAD SA STATISTIKOM NARUCIVANJA HRANE
(defn orders-per-month []
  (sql/query sql-db ["SELECT food_orders.id, month_name,COUNT(*) AS monthly_orders, SUM(price) AS total_price, SUM(amount) AS total_amount
                     FROM food_orders JOIN food_types ON food_orders.type_id =food_types.id
                     GROUP BY month_name ORDER BY total_price DESC"]))
(orders-per-month)

(defn general-food-statistic []
  (sql/query sql-db ["SELECT COUNT(*) AS total_num_orders, SUM(price) AS general_price, SUM(amount) AS general_amount
  FROM food_orders JOIN food_types ON food_orders.type_id =food_types.id"]))

(:total_num_orders (nth (general-food-statistic) 0))
(:general_price (nth (general-food-statistic) 0))
(:general_amount (nth (general-food-statistic) 0))
