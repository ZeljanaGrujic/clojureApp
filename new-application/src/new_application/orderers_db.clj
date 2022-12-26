(ns new-application.orderers-db
  (:require
    [clojure.java.jdbc :as sql]                             ;sql/sqlite database
    [clojure.pprint :as p]))

(def sql-db {
             :classname   "org.sqlite.JDBC"
             :subprotocol "sqlite"
             :subname     "resources/orderers.db"
             })


;(sql/db-do-commands sql-db
;                    "CREATE TABLE orderers (
;                    id integer primary key autoincrement,
;                     full_name varchar(255),
;                     amount varchar(255),
;                     do_date varchar(255),
;                     city_part varchar(255),
;                     street varchar(255),
;                     delivered varchar(50));"
;                    )



;;;RAD SA NARUDZBINAMA JAJA

(def orderers-coll "orderers")
(sql/query sql-db ["SELECT * FROM orderers"])
(def orders (sql/query sql-db ["SELECT * FROM orderers"]))

(p/print-table (sql/query sql-db ["SELECT * FROM orderers"]))

;(sql/insert! sql-db orderers-coll { :full_name "JESA" :amount "150" :do_date "23.12.2022", :city_part "Centar" :street "Bulevar 10" :delivered "NE"})
;(sql/delete! sql-db orderers-coll ["id= ?" 3])

(defn create-order [full_name amount do_date city_part street delivered]
  (sql/insert! sql-db orderers-coll { :full_name full_name :amount amount :do_date do_date :city_part city_part :street street :delivered delivered}))

(defn new-order [order]
  (sql/execute! sql-db ["INSERT INTO orderers (full_name, amount, do_date, city_part, street, delivered) VALUES (?, ?, ?, ?, ?, ?) "  (:full_name order) (:amount order) (:do_date order) (:city_part order) (:street order) (:delivered order)]))

;(create-order "NECA" "200" "23.12.2022" "HIM" "Visnjicka 23" "NE")

(defn edit-order [order]
  (sql/execute! sql-db ["UPDATE orderers  SET full_name = ? WHERE id = ?"  (:full_name order) (:id order)])
  (sql/execute! sql-db ["UPDATE orderers  SET amount = ? WHERE id = ?"  (:amount order) (:id order)])
  (sql/execute! sql-db ["UPDATE orderers  SET do_date = ? WHERE id = ?"  (:do_date order) (:id order)])
  (sql/execute! sql-db ["UPDATE orderers  SET city_part = ? WHERE id = ?"  (:city_part order) (:id order)])
  (sql/execute! sql-db ["UPDATE orderers  SET street = ? WHERE id = ?"  (:street order) (:id order)])
  (sql/execute! sql-db ["UPDATE orderers  SET delivered = ? WHERE id = ?"  (:delivered order) (:id order)]))

(defn list-orders [] (sql/query sql-db ["SELECT * FROM orderers"]))
;(for [o (list-orders)]
;  (println (:full_name o)))

(defn list-delivered-orders [] (sql/query sql-db ["SELECT * FROM orderers WHERE delivered='DA'"]))
(list-delivered-orders)

(defn get-order-by-id [id]
  (nth (filter #(= (:id %) id) (sql/query sql-db ["SELECT * FROM orderers"])) 0))
(get-order-by-id 2)

(defn get-order-by-name [full_name]
  (sql/query sql-db ["SELECT * FROM orderers WHERE full_name= ?" full_name]))
(get-order-by-name "NECA")

(defn get-next-id []
  (+ 1 (:m (nth (sql/query sql-db ["SELECT MAX(id) as m FROM orderers"]) 0))))
(get-next-id)

(defn delete-order [order]
  (sql/execute! sql-db ["DELETE FROM orderers WHERE id = ?"(:id order)]))
;(delete-order {:id 3, :full_name "NECA", :amount "200", :do_date "23.12.2022", :location "HIM" :delivered "NE"})

;;;;;RAD SA STATISTIKOM NARUCIVANJA JAJA

(def orderers-coll "orderers")
(sql/query sql-db ["SELECT * FROM orderers"])

(defn undelivered-cp [cp]
  (sql/query sql-db ["SELECT * FROM orderers WHERE delivered = 'NE' AND city_part= ?" cp]))
(undelivered-cp "Kolonija")


(defn total-num-orders []
  (:total_num (nth (sql/query sql-db ["SELECT COUNT(*) as total_num FROM orderers"]) 0)))
(total-num-orders)

(defn total-num-delivered-orders []
  (:total_num (nth (sql/query sql-db ["SELECT COUNT(*) as total_num FROM orderers WHERE delivered='DA'"]) 0)))
(total-num-delivered-orders)

(defn total-num-undelivered-orders []
  (:total_num (nth (sql/query sql-db ["SELECT COUNT(*) as total_num FROM orderers WHERE delivered='NE'"]) 0)))
(total-num-undelivered-orders)

(defn orders-per-person []
  (sql/query sql-db ["SELECT full_name, COUNT(*) AS maked_orders FROM orderers GROUP BY LOWER(full_name) ORDER BY maked_orders DESC"]))
(orders-per-person)