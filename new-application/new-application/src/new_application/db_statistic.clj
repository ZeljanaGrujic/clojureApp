;OVO JE PISANO ZA SQLYog za koji mora da se pokrene MySql server
;;NECU RADITI SA OVIM FAJLOM, PREBACILA SAM SE NA SQLite

(ns new-application.db-statistic)
(require '[clojure.java.jdbc :as sql])

(def sql-db {
             :classname "com.mysql.jdbc.Driver"
             :subprotocol "mysql"
             :subname "//localhost:3306/myapp-test"
             :user "root"
             :password ""
             })

;;;;;RAD SA STATISTIKOM NARUCIVANJA JAJA

(def orderers-coll "orderers")
(sql/query sql-db ["SELECT * FROM orderers"])

(defn undelivered-cp [cp]
  (sql/query sql-db ["SELECT * FROM orderers WHERE delivered = 'NE' AND city_part= ?" cp]))
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

(defn orders-per-person []
  (sql/query sql-db ["SELECT full_name, COUNT(*) AS maked_orders FROM orderers GROUP BY LOWER(full_name) ORDER BY maked_orders DESC"]))
(orders-per-person)



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










;["Kolonija" "Centar" "HRS" "HIM", "VUK", "Naselje", "Mikulja", "Bolnica", "Gimnazija", "Opeka", "Jezero" "Suleiceva" "Zelengora" "GOSA" "Kiseljak"]

;
;(defn undelivered-Kolonija []
;  (sql/query sql-db ["SELECT * FROM orderers WHERE delivered = 'NE' AND city_part= 'Kolonija'"]))
;(undelivered-Kolonija)
;
;(defn undelivered-Centar []
;  (sql/query sql-db ["SELECT * FROM orderers WHERE delivered = 'NE' AND city_part= 'Centar'"]))
;(defn undelivered-HRS []
;  (sql/query sql-db ["SELECT * FROM orderers WHERE delivered = 'NE' AND city_part= 'HRS'"]))
;(defn undelivered-HIM []
;  (sql/query sql-db ["SELECT * FROM orderers WHERE delivered = 'NE' AND city_part= 'HIM'"]))
;(defn undelivered-VUK []
;  (sql/query sql-db ["SELECT * FROM orderers WHERE delivered = 'NE' AND city_part= 'VUK'"]))
;(undelivered-VUK)
;(defn undelivered-Naselje []
;  (sql/query sql-db ["SELECT * FROM orderers WHERE delivered = 'NE' AND city_part= 'Naselje'"]))
;(defn undelivered-Mikulja []
;  (sql/query sql-db ["SELECT * FROM orderers WHERE delivered = 'NE' AND city_part= 'Mikulja'"]))
;(defn undelivered-Bolnica []
;  (sql/query sql-db ["SELECT * FROM orderers WHERE delivered = 'NE' AND city_part= 'Bolnica'"]))
;(defn undelivered-Gimnazija []
;  (sql/query sql-db ["SELECT * FROM orderers WHERE delivered = 'NE' AND city_part= 'Gimnazija'"]))
;(defn undelivered-Opeka []
;  (sql/query sql-db ["SELECT * FROM orderers WHERE delivered = 'NE' AND city_part= 'Opeka'"]))
;(defn undelivered-Jezero []
;  (sql/query sql-db ["SELECT * FROM orderers WHERE delivered = 'NE' AND city_part= 'Jezero'"]))
;(defn undelivered-Suleiceva []
;  (sql/query sql-db ["SELECT * FROM orderers WHERE delivered = 'NE' AND city_part= 'Suleiceva'"]))
;(defn undelivered-Zelengora []
;  (sql/query sql-db ["SELECT * FROM orderers WHERE delivered = 'NE' AND city_part= 'Zelengora'"]))
;(defn undelivered-GOSA []
;  (sql/query sql-db ["SELECT * FROM orderers WHERE delivered = 'NE' AND city_part= 'GOSA'"]))
;(defn undelivered-Kiseljak []
;  (sql/query sql-db ["SELECT * FROM orderers WHERE delivered = 'NE' AND city_part= 'Kiseljak'"]))