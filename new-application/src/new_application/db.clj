(ns new-application.db)
(require '[clojure.java.jdbc :as sql])


(def sql-db {
             :classname "com.mysql.jdbc.Driver"
             :subprotocol "mysql"
             :subname "//localhost:3306/myapp-test"
             :user "root"
             :password ""
             })

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



;;; POKUSATI DA NAPRAVIS BAZU PREKO ATOMA

(def data (atom { :orders [{:full_name "Srecko Grujic" :amount 0 :do_date "00.00.0000"}]}))
(swap! data update :orders conj { :full_name "Jesa" :amount 150 :do_date "23.12.2022"})
(swap! data update :orders conj { :full_name "NECA" :amount 200 :do_date "23.12.2022"})

(:orders (deref data))
(def orders-data (:orders (deref data)))

