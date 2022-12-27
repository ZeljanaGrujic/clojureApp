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
;                    "DROP TABLE orders_types")

;(sql/db-do-commands sql-db
;                    "CREATE TABLE orderers (
;                    id integer primary key autoincrement,
;                     full_name varchar(255),
;                     do_date varchar(255),
;                     city_part varchar(255),
;                     street varchar(255),
;                     delivered varchar(50),
;                     amount_id int );"
;                    )

;(sql/db-do-commands sql-db
;                    "CREATE TABLE orders_types (
;                    id integer primary key autoincrement,
;                     package_name varchar(255),
;                     amount int,
;                     price double );"
;                    )


;;;RAD SA NARUDZBINAMA JAJA

(def orderers-coll "orderers")
(sql/query sql-db ["SELECT * FROM orderers"])
(def orders (sql/query sql-db ["SELECT * FROM orderers"]))


(p/print-table (sql/query sql-db ["SELECT * FROM orders_types"]))

(defn create-type-order [t-order]
  (sql/execute! sql-db ["INSERT INTO orders_types (package_name, amount, price) VALUES (?, ?, ?) "  (:package_name t-order) (:amount t-order) (:price t-order)]))
;(create-type-order {:package_name "10komada" :amount 10 :price 130.0})
;(create-type-order {:package_name "20komada" :amount 20 :price 260.0})
;(create-type-order {:package_name "30komada" :amount 30 :price 370.0})
;(create-type-order {:package_name "40komada" :amount 40 :price 500.0})
;(create-type-order {:package_name "60komada" :amount 60 :price 760.0})
;(create-type-order {:package_name "70komada" :amount 70 :price 890.0})
;(create-type-order {:package_name "100komada" :amount 100 :price 1150.0})
;(create-type-order {:package_name "120komada" :amount 120 :price 1250.0})
;(create-type-order {:package_name "150komada" :amount 150 :price 1350.0})
;(create-type-order {:package_name "180komada" :amount 180 :price 1350.0})
;(create-type-order {:package_name "210komada" :amount 210 :price 1450.0})
;(create-type-order {:package_name "240komada" :amount 240 :price 1550.0})
;(create-type-order {:package_name "270komada" :amount 270 :price 1650.0})
;(create-type-order {:package_name "300komada" :amount 300 :price 1750.0})

(defn get-id-by-type-order [type_order]
  (:id (nth (sql/query sql-db ["SELECT id FROM orders_types WHERE package_name=?"type_order]) 0)))
(get-id-by-type-order "10komada")

(p/print-table (sql/query sql-db ["SELECT * FROM orderers"]))

;(sql/insert! sql-db orderers-coll { :full_name "JESA" :amount "150" :do_date "23.12.2022", :city_part "Centar" :street "Bulevar 10" :delivered "NE"})
;(sql/delete! sql-db orderers-coll ["id= ?" 3])

(defn create-order [full_name do_date city_part street delivered package_name]
  (sql/insert! sql-db orderers-coll { :full_name full_name :do_date do_date :city_part city_part :street street :delivered delivered :amount_id (get-id-by-type-order package_name)}))

(defn new-order [{id :id full_name :full_name do_date :do_date city_part :city_part street :street delivered :delivered package_name :package_name}]
  (sql/execute! sql-db ["INSERT INTO orderers (full_name, do_date, city_part, street, delivered, amount_id) VALUES (?, ?, ?, ?, ?, ?) "  full_name do_date city_part street delivered (get-id-by-type-order package_name)]))

;(create-order "NECA" "27.12.2022." "HIM" "Udarnih brigada 3" "NE" "300 komada")
;(new-order {:full_name "Zeljana" :do_date "27.12.2022." :city_part "HRS" :street "Ustanicka 5" :delivered "NE" :package_name "10komada"})

(defn edit-order [order]
  (sql/execute! sql-db ["UPDATE orderers  SET full_name = ? WHERE id = ?"  (:full_name order) (:id order)])
  (sql/execute! sql-db ["UPDATE orderers  SET do_date = ? WHERE id = ?"  (:do_date order) (:id order)])
  (sql/execute! sql-db ["UPDATE orderers  SET city_part = ? WHERE id = ?"  (:city_part order) (:id order)])
  (sql/execute! sql-db ["UPDATE orderers  SET street = ? WHERE id = ?"  (:street order) (:id order)])
  (sql/execute! sql-db ["UPDATE orderers  SET delivered = ? WHERE id = ?"  (:delivered order) (:id order)])
  (sql/execute! sql-db ["UPDATE orderers  SET amount_id = ? WHERE id = ?"  (get-id-by-type-order (:package_name order)) (:id order)]))

;(edit-order {:id 2 :full_name "JESA" :do_date "27.12.2022." :city_part "Centar" :street "Ustanicka 5" :delivered "NE" :package_name "270 komada"})

(defn list-orders [] (sql/query sql-db ["SELECT * FROM orderers"]))

(defn list-orders-all-info []
  (sql/query sql-db ["SELECT orderers.id, full_name, do_date, city_part, street, delivered, amount, price
   FROM orderers JOIN orders_types ON orderers.amount_id = orders_types.id"]))
(list-orders-all-info)

;(for [o (list-orders)]
;  (println (:full_name o)))

(defn list-delivered-orders [] (sql/query sql-db ["SELECT * FROM orderers WHERE delivered='DA'"]))
(list-delivered-orders)

(defn get-order-by-id [id]
  (nth (filter #(= (:id %) id) (sql/query sql-db ["SELECT orderers.id, full_name, do_date, city_part, street, delivered, amount, price
   FROM orderers JOIN orders_types ON orderers.amount_id = orders_types.id"])) 0))
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
  (sql/query sql-db ["SELECT orderers.id, full_name, do_date, city_part, street, delivered, amount, price
   FROM orderers JOIN orders_types ON orderers.amount_id = orders_types.id WHERE delivered = 'NE' AND city_part= ?" cp]))
(undelivered-cp "Centar")


(defn total-num-orders []
  (:total_num (nth (sql/query sql-db ["SELECT COUNT(*) as total_num FROM orderers"]) 0)))
(total-num-orders)

(defn total-num-delivered-orders []
  (:total_num (nth (sql/query sql-db ["SELECT COUNT(*) as total_num FROM orderers WHERE delivered='DA'"]) 0)))
(total-num-delivered-orders)

(defn total-num-undelivered-orders []
  (:total_num (nth (sql/query sql-db ["SELECT COUNT(*) as total_num FROM orderers WHERE delivered='NE'"]) 0)))
(total-num-undelivered-orders)


(defn full_order_info [id]
  (sql/query sql-db ["SELECT orderers.id, full_name, do_date, city_part, street, delivered, amount, price
   FROM orderers JOIN orders_types ON orderers.amount_id = orders_types.id WHERE orderers.id=?" id]))
(full_order_info 1)


(defn orders-per-person []
  (sql/query sql-db ["SELECT full_name, COUNT(*) AS maked_orders, SUM(amount) as total_amount, SUM(price) as total_price
   FROM orderers JOIN orders_types ON orderers.amount_id = orders_types.id GROUP BY LOWER(full_name) ORDER BY total_amount DESC"]))
(orders-per-person)