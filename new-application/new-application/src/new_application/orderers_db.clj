(ns new-application.orderers-db
  (:require
    [clojure.java.jdbc :as sql]                             ;sql/sqlite database
    [clojure.pprint :as p]
    ;[new-application.users-db :as udb]
    ; [new-application.users-db :as udb]
    ))

(def sql-db {
             :classname   "org.sqlite.JDBC"
             :subprotocol "sqlite"
             :subname     "resources/orderers.db"
             })


;(sql/db-do-commands sql-db
;                    "DROP TABLE notes")

;(sql/db-do-commands sql-db
;                    "CREATE TABLE orderers (
;                    id integer primary key autoincrement,
;                     full_name varchar(255),
;                     do_date varchar(255),
;                     city_part varchar(255),
;                     street varchar(255),
;                     delivered varchar(50),
;                     amount_id int,
;                     user_id int );"
;                    )

;(sql/db-do-commands sql-db
;                    "CREATE TABLE orders_types (
;                    id integer primary key autoincrement,
;                     package_name varchar(255),
;                     amount int,
;                     price double );"
;                    )

;(sql/db-do-commands sql-db
;                    "CREATE TABLE users (
;                    id integer primary key autoincrement,
;                     owner_name varchar(255),
;                     owner_surname varchar(255),
;                     phone varchar(255),
;                     password varchar(255) );"
;                    )

;(sql/db-do-commands sql-db
;                    "CREATE TABLE notes (
;                    id integer primary key autoincrement,
;                     message_body varchar(255),
;                     user_id int,
;                     read varchar(255) default 'NE' );"
;                    )

;;WORKING WITH USERS
(p/print-table (sql/query sql-db ["SELECT * FROM users"]))
;;bez obzira da li se neko registrovao kao vec postojeci korinsik nema veze jer svakako ja nalazim prvog koji zadovoljava uslov
;polazimo od pretpostavke da osobe imaju jedinstveni broj telefona, tako da ako neko i ima istu sifru nije vazno, nece imati isti br telefona


;;vrsicu proveru preko password i phone
(defn create-user [user]
  (sql/execute! sql-db ["INSERT INTO users (owner_name, owner_surname, phone, password) VALUES (?, ?, ?, ?) " (:owner_name user) (:owner_surname user) (:phone user) (:password user)]))
;(create-user {:owner_name "Biljana" :owner_surname "Grujic" :phone "0600133611" :password "2Fr4AA"})
;(create-user {:owner_name "Radmilo" :owner_surname "Stojanovic" :phone "063-332-813" :password "radmilo"})
;(create-user {:owner_name "Mileva" :owner_surname "Stojanovic" :phone "060-0000-002" :password "mileva"})

(defn count-users []
  (:num (nth (sql/query sql-db ["SELECT COUNT(*) as num FROM users"]) 0)))
;(count-users)

(defn check-credentials [user]
  (nth (sql/query sql-db ["SELECT phone, password FROM users WHERE phone=? AND password=?" (:phone user) (:password user)]) 0))
;(if (= null (check-credentials {:phone "060-0323-058" :password "zeljan111a" })))
;  "Greska") ;ako ne nadje vraca () praznu listu, ako nadje vraca celog usera
;(check-credentials {:phone "060-0000-999" :password "test" })

(defn if-exist [user]
  (sql/query sql-db ["SELECT * FROM users WHERE phone=?" (:phone user)]))
;(if-exist {:owner_name "Biljana" :owner_surname "Grujic" :phone "060013361111" :password "2Fr4AA"})

;(defn check-credentials1 [user]
;  (sql/query sql-db ["SELECT phone, password FROM users WHERE phone=? AND password=?" (:phone user) (:password user)]))
;;(check-credentials1 {:phone "060-0323-058" :password "zeljana" })


(defn get-next-user-id []
  (+ 1 (:m (nth (sql/query sql-db ["SELECT MAX(id) as m FROM users"]) 0))))
;(get-next-user-id)

(defn get-user-id-by-phone [phone]
  (:id (nth (sql/query sql-db ["SELECT id FROM users WHERE phone=?" phone]) 0)))
;(get-user-id-by-phone "060-0000-000")




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
;(get-id-by-type-order "10komada")

(p/print-table (sql/query sql-db ["SELECT * FROM orderers"]))

;(sql/insert! sql-db orderers-coll { :full_name "JESA" :amount "150" :do_date "23.12.2022", :city_part "Centar" :street "Bulevar 10" :delivered "NE"})
;(sql/delete! sql-db orderers-coll ["id= ?" 3])

;(defn create-order [full_name do_date city_part street delivered package_name]
;  (sql/insert! sql-db orderers-coll { :full_name full_name :do_date do_date :city_part city_part :street street :delivered delivered :amount_id (get-id-by-type-order package_name)}))

(defn new-order [{id :id full_name :full_name do_date :do_date city_part :city_part street :street delivered :delivered package_name :package_name phone :phone}]
  (sql/execute! sql-db ["INSERT INTO orderers (full_name, do_date, city_part, street, delivered, amount_id, user_id) VALUES (?, ?, ?, ?, ?, ?,?) "  full_name do_date city_part street delivered (get-id-by-type-order package_name) (get-user-id-by-phone phone)]))

;(new-order  { :full_name "Zeljana Grujic" :do_date "7.1.2023." :city_part "HRS" :street "Slobodana Jovanovica 6" :delivered "NE" :package_name "20komada" :phone "060-0323-058"})
;(new-order {:full_name "Test order" :do_date "4.1.2023." :city_part "HRS" :street "Ustanicka 5" :delivered "NE" :package_name "10komada" :phone "060-0000-000"})

(defn edit-order [order]
  (sql/execute! sql-db ["UPDATE orderers  SET full_name = ? WHERE id = ?"  (:full_name order) (:id order)])
  (sql/execute! sql-db ["UPDATE orderers  SET do_date = ? WHERE id = ?"  (:do_date order) (:id order)])
  (sql/execute! sql-db ["UPDATE orderers  SET city_part = ? WHERE id = ?"  (:city_part order) (:id order)])
  (sql/execute! sql-db ["UPDATE orderers  SET street = ? WHERE id = ?"  (:street order) (:id order)])
  (sql/execute! sql-db ["UPDATE orderers  SET delivered = ? WHERE id = ?"  (:delivered order) (:id order)])
  (sql/execute! sql-db ["UPDATE orderers  SET amount_id = ? WHERE id = ?"  (get-id-by-type-order (:package_name order)) (:id order)]))

;(edit-order {:id 22 :full_name "Test order edit" :do_date "5.1.2023." :city_part "HRS" :street "Ustanicka 5" :delivered "NE" :package_name "10komada"})

(defn list-orders [] (sql/query sql-db ["SELECT * FROM orderers WHERE full_name NOT LIKE 'Test order'"]))
;(list-orders)

(defn list-orders-all-info []
  (sql/query sql-db ["SELECT orderers.id, full_name, do_date, city_part, street, delivered, amount, price
   FROM orderers JOIN orders_types ON orderers.amount_id = orders_types.id"]))

(defn list-orders-by-name [full_name]
  (sql/query sql-db ["SELECT orderers.id, full_name, do_date, city_part, street, delivered, amount, price
   FROM orderers JOIN orders_types ON orderers.amount_id = orders_types.id WHERE full_name=?" full_name]))


(defn list-orderers-names []
  (sql/query sql-db ["SELECT DISTINCT full_name FROM orderers WHERE full_name NOT LIKE 'Test order'"]))
(list-orderers-names)


(list-orders-all-info)

;(for [o (list-orders)]
;  (println (:full_name o)))

(defn list-delivered-orders [] (sql/query sql-db ["SELECT * FROM orderers WHERE delivered='DA'"]))
(list-delivered-orders)


(defn get-order-by-id [id]
  (nth (filter #(= (:id %) id) (sql/query sql-db ["SELECT orderers.id, full_name, do_date, city_part, street, delivered, amount, price
   FROM orderers JOIN orders_types ON orderers.amount_id = orders_types.id"])) 0))
;(get-order-by-id 3)

(defn get-order-by-id1 [id]
  (nth (filter #(= (:id %) id) (sql/query sql-db ["SELECT orderers.id as id, full_name, do_date, city_part, street, delivered, amount, price, phone
   FROM orderers  JOIN orders_types ON orderers.amount_id = orders_types.id JOIN users ON orderers.user_id= users.id"])) 0))


(p/print-table (sql/query sql-db ["SELECT orderers.id, full_name, do_date, city_part, street, delivered, user_id, amount, price
   FROM orderers JOIN orders_types ON orderers.amount_id = orders_types.id JOIN users ON orderers.user_id= users.id"]))

;(get-order-by-id1 1)

(defn get-order-by-name [full_name]
  (sql/query sql-db ["SELECT * FROM orderers WHERE full_name= ?" full_name]))
;(get-order-by-name "Test order edit")

(defn get-next-id []
  (+ 1 (:m (nth (sql/query sql-db ["SELECT MAX(id) as m FROM orderers"]) 0))))
;(get-next-id)

(defn delete-order [order]
  (sql/execute! sql-db ["DELETE FROM orderers WHERE id = ?"(:id order)]))
;(delete-order {:id 23, :full_name "Test order edit", :amount "10", :do_date "5.1.2023.", :location "HRS" :delivered "NE"})

;;;;;RAD SA STATISTIKOM NARUCIVANJA JAJA

(def orderers-coll "orderers")
(sql/query sql-db ["SELECT * FROM orderers"])



(defn undelivered-cp [cp]
  (sql/query sql-db ["SELECT orderers.id, full_name, do_date, city_part, street, delivered, amount, price
   FROM orderers JOIN orders_types ON orderers.amount_id = orders_types.id WHERE full_name NOT LIKE 'Test order' AND delivered = 'NE' AND city_part= ?" cp]))
;(undelivered-cp "Centar")


(defn total-num-orders []
  (:total_num (nth (sql/query sql-db ["SELECT COUNT(*) as total_num FROM orderers"]) 0)))

(defn total-num-delivered-orders []
  (:total_num (nth (sql/query sql-db ["SELECT COUNT(*) as total_num FROM orderers WHERE delivered='DA'"]) 0)))
(total-num-delivered-orders)

(defn total-num-undelivered-orders []
  (:total_num (nth (sql/query sql-db ["SELECT COUNT(*) as total_num FROM orderers WHERE delivered='NE'"]) 0)))
(total-num-undelivered-orders)

(defn total-income []
  (:total_income (nth (sql/query sql-db ["SELECT SUM(price) as total_income FROM orderers JOIN orders_types ON orderers.amount_id = orders_types.id "]) 0)))

(total-income)

(defn full_order_info [id]
  (sql/query sql-db ["SELECT orderers.id, full_name, do_date, city_part, street, delivered, amount, price
   FROM orderers JOIN orders_types ON orderers.amount_id = orders_types.id WHERE orderers.id=?" id]))
;(full_order_info 1)


(defn orders-per-person []
  (sql/query sql-db ["SELECT full_name, COUNT(*) AS maked_orders, SUM(amount) as total_amount, SUM(price) as total_price
   FROM orderers JOIN orders_types ON orderers.amount_id = orders_types.id WHERE full_name NOT LIKE 'Test order' GROUP BY LOWER(full_name) ORDER BY total_amount DESC"]))
(orders-per-person)

(defn get-id-by-phone [ph]
  (:id (nth (sql/query sql-db ["SELECT id FROM users where phone=?" ph]) 0)))
;(get-id-by-phone "060-0000-000")

(defn get-name-by-id-session [id]
  (:owner_name (nth (sql/query sql-db ["SELECT owner_name FROM users where id=?" id]) 0)))

(defn get-surname-by-id-session [id]
  (:owner_surname (nth (sql/query sql-db ["SELECT owner_surname FROM users where id=?" id]) 0)))
;(get-surname-by-id-session 22)

(defn get-phone-by-id-session [id]
  (:phone (nth (sql/query sql-db ["SELECT phone FROM users where id=?" id]) 0)))
;(get-phone-by-id-session 22)

(defn user-orders-session [id]
  (sql/query sql-db ["SELECT full_name,do_date,street, amount_id, user_id, package_name, price
   FROM orderers JOIN orders_types ON orderers.amount_id = orders_types.id WHERE user_id=?" id]))
;(user-orders-session 5)


;;;;;WORKING WITH USER NOTES

(p/print-table (sql/query sql-db ["SELECT * FROM notes"]))

(defn create-note [note]
  (sql/execute! sql-db ["INSERT INTO notes (message_body, user_id) VALUES (?, ?)" (:message_body note) (:user_id note)]))
;(create-note {:message_body "Ponistiti narudzbinu za 6.1.2023 od 10kom" :user_id 5})
;create note vraca seq od 1

(defn list-unread-notes [] (sql/query sql-db ["SELECT owner_name, owner_surname, phone, notes.id, message_body FROM notes JOIN users ON notes.user_id= users.id WHERE notes.read= 'NE'"]))
(list-unread-notes)
(defn list-read-notes [] (sql/query sql-db ["SELECT owner_name, owner_surname, phone, notes.id, message_body FROM notes JOIN users ON notes.user_id= users.id WHERE message_body NOT LIKE 'Test note' AND notes.read= 'DA'"]))
(list-read-notes)

(defn get-note-by-id [id]
  (nth (filter #(= (:id %) id) (sql/query sql-db ["SELECT owner_name, owner_surname, phone, notes.id, message_body,read FROM notes JOIN users ON notes.user_id= users.id WHERE notes.id=?" id])) 0))
;(get-note-by-id 3)

(defn get-user-id-by-id-session [id]
  (:id (nth (sql/query sql-db ["SELECT id FROM users where id=?" id]) 0)))

(defn edit-note [note]
  (sql/execute! sql-db ["UPDATE notes  SET message_body=? WHERE id =?"  (:message_body note) (:id note)])
  (sql/execute! sql-db ["UPDATE notes  SET read=? WHERE id =?"  (:read note) (:id note)]))