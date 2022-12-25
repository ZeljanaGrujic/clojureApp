(ns new-application.db)
(require '[clojure.java.jdbc :as sql])


(def sql-db {
             :classname "com.mysql.jdbc.Driver"
             :subprotocol "mysql"
             :subname "//localhost:3306/myapp-test"
             :user "root"
             :password ""
             })


;;;RAD SA NARUDZBINAMA JAJA

(def orderers-coll "orderers")

(sql/query sql-db ["SELECT * FROM orderers"])
(def orders (sql/query sql-db ["SELECT * FROM orderers"]))

(sql/insert! sql-db orderers-coll { :full_name "JESAaaa" :amount "150" :do_date "23.12.2022", :city_part "Centar" :street "Bulevar 10" :delivered "NE"})
(sql/delete! sql-db orderers-coll ["id= ?" 3])


(defn create-order [full_name amount do_date city_part street delivered]
  (sql/insert! sql-db orderers-coll { :full_name full_name :amount amount :do_date do_date :city_part city_part :street street :delivered delivered}))

(defn new-order [order]
  (sql/execute! sql-db ["INSERT INTO orderers (full_name, amount, do_date, city_part, street, delivered) VALUES (?, ?, ?, ?, ?, ?) "  (:full_name order) (:amount order) (:do_date order) (:city_part order) (:street order) (:delivered order)]))

(create-order "NECA" "200" "23.12.2022" "HIM" "Tralalala" "NE")

(defn edit-order [order]
  (sql/execute! sql-db ["UPDATE orderers  SET full_name = ? WHERE id = ?"  (:full_name order) (:id order)])
  (sql/execute! sql-db ["UPDATE orderers  SET amount = ? WHERE id = ?"  (:amount order) (:id order)])
  (sql/execute! sql-db ["UPDATE orderers  SET do_date = ? WHERE id = ?"  (:do_date order) (:id order)])
  (sql/execute! sql-db ["UPDATE orderers  SET city_part = ? WHERE id = ?"  (:city_part order) (:id order)])
  (sql/execute! sql-db ["UPDATE orderers  SET street = ? WHERE id = ?"  (:street order) (:id order)])
  (sql/execute! sql-db ["UPDATE orderers  SET delivered = ? WHERE id = ?"  (:delivered order) (:id order)]))

(defn list-orders [] (sql/query sql-db ["SELECT * FROM orderers"]))
(for [o (list-orders)]
  (println (:full_name o)))

(defn list-delivered-orders [] (sql/query sql-db ["SELECT * FROM orderers WHERE delivered='DA'"]))
(list-delivered-orders)

(defn get-order-by-id [id]
  (nth (filter #(= (:id %) id) (sql/query sql-db ["SELECT * FROM orderers"])) 0))
(get-order-by-id 9)

(defn get-order-by-name [full_name]
  (sql/query sql-db ["SELECT * FROM orderers WHERE full_name= ?" full_name]))
(get-order-by-name "NECA")

(defn get-next-id []
  (+ 1 (:m (nth (sql/query sql-db ["SELECT MAX(id) as m FROM orderers"]) 0))))
(get-next-id)

(defn delete-order [order]
  (sql/execute! sql-db ["DELETE FROM orderers WHERE id = ?"(:id order)]))
;(delete-order {:id 3, :full_name "NECA", :amount "200", :do_date "23.12.2022", :location "HIM" :delivered "NE"})


;;;;;;;RAD SA NARUCIVANJEM HRANE

(defn get-id-by-type-name [type_name]
  (:id (nth (sql/query sql-db ["SELECT id FROM food_types WHERE type_name=?"  type_name]) 0)))
(get-id-by-type-name "Pantelic zito")
(get-id-by-type-name "Pantelic vitamini")


(defn new-food-order [{id :id amount :amount do_date :do_date month_name :month_name type_name :type_name}]
  (sql/execute! sql-db ["INSERT INTO food_orders (do_date, month_name, type_id) VALUES (?, ?, ?) " do_date month_name (get-id-by-type-name type_name)]))

(new-food-order {:do_date "28.12.2022" :month_name "December" :type_name "Pantelic zito"})

(defn get-next-food-id []
  (+ 1 (:m (nth (sql/query sql-db ["SELECT MAX(id) as m FROM food_orders"]) 0))))
(get-next-food-id)

(defn list-full-forders [month_name]
  (sql/query sql-db ["SELECT food_orders.id, food_orders.amount, food_orders.do_date, food_orders.month_name, food_types.type_name FROM food_orders JOIN food_types ON food_orders.type_id =food_types.id WHERE month_name= ?"month_name]))

(defn list-full-forders-delete []
  (sql/query sql-db ["SELECT food_orders.id, food_orders.amount, food_orders.do_date, food_orders.month_name, food_types.type_name FROM food_orders JOIN food_types ON food_orders.type_id =food_types.id"]))

(list-full-forders "Januar")
(list-full-forders-delete)




;;; POKUSATI DA NAPRAVIS BAZU PREKO ATOMA

(def data (atom { :orders [{:full_name "Srecko Grujic" :amount 0 :do_date "00.00.0000"}]}))
(swap! data update :orders conj { :full_name "Jesa" :amount 150 :do_date "23.12.2022"})
(swap! data update :orders conj { :full_name "NECA" :amount 200 :do_date "23.12.2022"})

(:orders (deref data))
(def orders-data (:orders (deref data)))

