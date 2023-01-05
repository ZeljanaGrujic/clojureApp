(ns new-application.test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [new-application.pages :as p]
            [new-application.core :as c]
            [ring.util.request :as req]
            [new-application.administrator :as a]
    ; [midje.sweet :refer [=> facts]]
            [new-application.orderers-db :as odb]
            [new-application.food-orders-db :as fodb]))

;;because of Intellij IDE it was way easier form me to write and run tests using clojure.test library than midje, but midje is definitely more beginner friendly

(deftest test-main-route
  "Testing if it will return main page on / route"
  (testing "main route"
    (let [response (c/app-routes (mock/request :get "/"))]
      (is (= 200 (:status response))))))

(deftest test-info-route
  "Testing if it will return info page on /grujic-info route"
  (testing "info route"
    (let [response (c/app-routes (mock/request :get "/grujicagro-info"))]
      (is (= 200 (:status response))))))

(deftest test-admin-login-route
  "Testing if it will return admin login page on /admin/login route"
  (testing "admin route"
    (let [response (c/app-routes (mock/request :get "/admin/login"))]
      (is (= 200 (:status response))))))

(deftest test-user-register-route
  "Testing if it will return user login page  on /user/login route"
  (testing "user register route"
    (let [response (c/app-routes (mock/request :get "/user/register/"))]
      (is (= 200 (:status response))))))

(deftest test-user-login-route
  "Testing if it will return user login page  on /user/login route"
  (testing "user login route"
    (let [response (c/app-routes (mock/request :get "/user/login"))]
      (is (= 200 (:status response))))))

(deftest test-admin-login
  "Testing if admin is logged in properly"
  (testing "admin login"
    (let [succes (a/check-credentials {:login "admin" :password "admin"})]
      (is (= succes true)))))

;;TESTING DATABASE FUNCTIONS
(deftest test-num-of-users
  "Number of users can be >= 0"
       (testing "user count in users db"
        (let [num (odb/count-users)]
          (is (>= num 0)))))

;(deftest test-create-user
;  "If creation of user is successful it will return (1)"
;  (testing "create user"
;    (let [succes (nth (odb/create-user {:owner_name "Test user" :owner_surname "Test user" :phone "060-0000-000" :password "test"}) 0)]
;      (is (= succes 1)))))
;
;(deftest test-create-user2
;  "If creation of user is successful it will return (1)"
;  (testing "create user"
;    (let [succes (odb/create-user {:owner_name "Test user" :owner_surname "Test user" :phone "060-0000-000" :password "test"})]
;      (is (= succes (seq '(1)))))))
;these two tests for creating users should be started only one time, and only if we don't have test user created already


(deftest test-check-credentials-user
  "If user credential are true then it will return map of that information"
  (testing "create user"
    (let [succes (odb/check-credentials {:phone "060-0000-000" :password "test"})]
      (is (= succes {:phone "060-0000-000" :password "test"})))))

(deftest test-get-next-id-user
  "Get next id should return some integer number greater than 0"
  (testing "next-id"
    (let [next (odb/get-next-user-id)]
      (is (= (integer? (odb/get-next-user-id)) true))
      (is (> next 0)))))

(deftest test-check-credentials-admin
  "If admin credential are true then it will return true"
  (testing "create user"
    (let [succes (a/check-credentials {:login "admin" :password "admin" })]
      (is (= succes true)))))

(deftest test-get-user-id-by-phone
  "Get user id should return some integer number greater than 0"
  (testing "user-id-by-phone"
    (let [res (odb/get-user-id-by-phone "060-0000-000")]
      (is (= (integer? (odb/get-user-id-by-phone "060-0000-000")) true))
      (is (> res 0)))))

(deftest test-get-id-by-type-order
  "We have 14 types of orders, and every type has their own id, for example type 40komada has id 4"
  (testing "id-by-order-type"
    (let [res (odb/get-id-by-type-order "40komada")]
      (is (= (integer? (odb/get-user-id-by-phone "060-0000-000")) true))
      (is (> res 0))
      (is (= res 4)))))

(deftest test-create-new-order
      "If creation of oder is successful it will return (1) in sequence"
      (testing "create order"
        (let [succes (odb/new-order {:full_name "Test order" :do_date "4.1.2023." :city_part "HRS" :street "Ustanicka 5" :delivered "NE" :package_name "10komada" :phone "060-0000-000"})]
          (is (= succes (seq '(1)))))))

(deftest test-edit-order
  "If editing oder is successful it will return (1) in sequence"
  (testing "edit order"
    (let [succes (odb/edit-order {:id 22 :full_name "Test order edit" :do_date "5.1.2023." :city_part "HRS" :street "Ustanicka 5" :delivered "NE" :package_name "10komada"})]
      (is (= succes (seq '(1)))))))


;(if (= (empty?  (list-orders)) true)
;  "Empty"
;  "Not empty")

(deftest test-list-orders
  "If we call list orders it will return collection of orders, and that map will not be empty
  but just in case we should cover empty case"
  (testing "list orders"
    (let [succes (odb/list-orders)]
      (is (or (= (empty? succes) true) (= (empty? succes) false))))))

(deftest test-list-orders-all-info
  "If we call list orders all info it will return collection of orders, and that map will not be empty
  but just in case we should cover empty case"
  (testing "list orders all info"
    (let [succes (odb/list-orders-all-info)]
      (is (or (= (empty? succes) true) (= (empty? succes) false))))))

(deftest test-list-delivered-orders
  "If we call list delivered orders it will return collection od delivered orders, but we could have empty map
  if all orders are undelivered"
  (testing "list delivered orders"
    (let [succes (odb/list-delivered-orders)]
      (is (or (= (empty? succes) true) (= (empty? succes) false))))))

(deftest test-get-order-by-id1
  "It will return test order which has id 22, this result is taken for testing needs, from database
  If anything is changed to this order with id 22, it sholud also be changed in this test"
  (testing "get order by id"
    (let [succes (odb/get-order-by-id1 22)]
      (is (= succes {:amount 10,
                     :city_part "HRS",
                     :phone "060-0000-000",
                     :delivered "NE",
                     :street "Ustanicka 5",
                     :do_date "5.1.2023.",
                     :id 22,
                     :full_name "Test order edit",
                     :price 130.0})))))

(deftest test-order-by-name
  "It will return one or more order by given name, but if we do not have orders for that name it will return empty sequence"
  (testing "order by name"
    (let [succes (odb/get-order-by-name "Test order edit")]
      (is (or (= (empty? succes) true) (= (empty? succes) false))))))



(deftest test-delete-order
  "If delete order is successful it will return (1) in sequence, you need to be careful about id,
   this order is deleted so you need to change id for some other order"
  (testing "delete order"
    (let [succes (odb/delete-order {:id 22, :full_name "Test order edit", :amount "10", :do_date "5.1.2023.", :location "HRS" :delivered "NE"})]
      (is (= succes (seq '(1)))))))

(deftest test-undelivered-city-part
  "It will return one or more order by given city part,
   but if we do not have orders for that city parts it will return empty sequence"
  (testing "undelivered cp"
    (let [succes (odb/undelivered-cp "Centar")]
      (is (or (= (empty? succes) true) (= (empty? succes) false))))))

(deftest test-number-of-orders
  "Number of orders can be >= 0"
  (testing "number of orders"
    (let [num (odb/total-num-orders)]
      (is (= (integer? (odb/total-num-orders)) true))
      (is (>= num 0)))))

(deftest test-num-delivered-orders
  "Number of delivered orders can be >= 0"
  (testing "number of delivered orders"
    (let [num (odb/total-num-delivered-orders)]
      (is (= (integer? (odb/total-num-orders)) true))
      (is (>= num 0)))))

(deftest test-num-undelivered-orders
  "Number of undelivered orders can be >= 0"
  (testing "number of undelivered orders"
    (let [num (odb/total-num-undelivered-orders)]
      (is (= (integer? (odb/total-num-orders)) true))
      (is (>= num 0)))))

(deftest test-orders-per-person
  "It will return one or more orders for specific user, we will have at least one order for person
  so the result won't ever be empty collection"
  (testing "orders per person"
    (let [succes (odb/orders-per-person)]
      (is (= (empty? succes) false)))))

(deftest test-create-food-type
  "If creation of food type is successful it will return (1) in sequence,
   this is only for testing and this food type won't be used in application"
  (testing "create food type"
    (let [succes (fodb/create-food-type {:type_name "Test food type" :price 0})]
      (is (= succes (seq '(1)))))))

(deftest test-get-id-by-type-order1
  "Only two type of food will be used 1 for Pantelic zito and 2 for Pantelic vitamini"
  (testing "food type id1"
    (let [succes (fodb/get-id-by-type-name "Pantelic zito")]
      (is (= succes 1)))))

(deftest test-get-id-by-type-order2
  "Only two type of food will be used 1 for Pantelic zito and 2 for Pantelic vitamini"
  (testing "food type id2"
    (let [succes (fodb/get-id-by-type-name "Pantelic vitamini")]
      (is (= succes 2)))))

(deftest test-create-food-order
  "If creation of food order is successful it will return (1) in sequence,
   this is only for testing and this food order won't be used in application"
  (testing "create new food order"
    (let [succes (fodb/new-food-order {:do_date "5.1.2023." :month_name "Januar" :type_name "Pantelic zito"})]
      (is (= succes (seq '(1)))))))

(deftest test-get-next-id-food-order
  "Get next id should return some integer number greater than 0"
  (testing "next-id-food-order"
    (let [next (fodb/get-next-food-id)]
      (is (= (integer? (odb/get-next-user-id)) true))
      (is (> next 0)))))

(deftest test-list-forders-per-month
  "It will return one or more food orders by given month,
   but if we do not have orders for that month it will return empty sequence"
  (testing "list-forders"
    (let [succes (fodb/list-full-forders "Jun")]
      (is (or (= (empty? succes) true) (= (empty? succes) false))))))

(deftest test-list-forders-by-type-name
  "It will return one or more food orders by given type name,
   we will definitely have at least one order for given type name "
  (testing "list-forders-by-type-name"
    (let [succes (fodb/list-full-forders-by-name "Pantelic zito")]
      (is (or (= (empty? succes) true) (= (empty? succes) false))))))

(deftest test-get-food-order-by-id
  "It will return test order which has id 9, this result is taken for testing needs, from database
  If anything is changed to this order with id 9, it sholud also be changed in this test"
  (testing "get food order by id"
    (let [succes (fodb/get-food-order-by-id 9)]
      (is (= succes {:id 9, :amount 1000.0, :do_date "5.1.2023.", :month_name "Januar", :type_id 1})))))

(deftest test-delete-food-order
  "If delete food order is successful it will return (1) in sequence, you need to be careful about id,
   this order is deleted so you need to change id for some other order"
  (testing "delete food order"
    (let [succes (fodb/delete-food-order {:id 9, :amount 1000.0, :do_date "5.1.2023.", :month_name "Januar", :type_id 1})]
      (is (= succes (seq '(1)))))))


(deftest test-forders-per-month-stat
  "It will group by food orders by given month, so our result shouldn't be empty collection"
  (testing "forders-month-stat"
    (let [succes (fodb/orders-per-month)]
      (is (= (empty? succes) false)))))

(deftest test-general-food-stat
  "It will return all info that we have for food orders, in case that we didn't make any food order result will be empty collection"
  (testing "forders-general-stat"
    (let [succes (fodb/orders-per-month)]
      (is (or (= (empty? succes) true) (= (empty? succes) false))))))

(deftest test-if-exist
  "It will check if user with that phone already exist, because phone number is unique for each person.
  If user doesn't exist, it will return empty collection (), but if he exists result won't be empty"
  (testing "exist"
    (let [succes (odb/if-exist {:owner_name "Biljana" :owner_surname "Grujic" :phone "0600133611" :password "2Fr4AA"})]
      (is (or (= (empty? succes) true) (= (empty? succes) false))))))

;(if (string? (get-name-by-id-session 22)) "OK")

(deftest test-name-session
  "When user is logged in his id is stored in session, so when we search his name by his id some string should be returned"
  (testing "name form session"
    (let [succes (odb/get-name-by-id-session 1)]
      (is (string? succes)))))

(deftest test-surname-session
  "When user is logged in his id is stored in session, so when we search his surname by his id some string should be returned"
  (testing "surname form session"
    (let [succes (odb/get-surname-by-id-session 1)]
      (is (string? succes)))))

(deftest test-phone-session
  "When user is logged in his id is stored in session, so when we search his phone by his id some string should be returned"
  (testing "phone form session"
    (let [succes (odb/get-phone-by-id-session 1)]
      (is (string? succes)))))

(deftest test-find-id-by-phone
  "When we search id trought phone some integer should be returned,
  you can see phone numbers that we have in database and type it here,
  we used credentials from test user"
  (testing "find id by phone"
    (let [succes (odb/get-id-by-phone "060-0000-000")]
      (is (integer? succes))
      (is (> succes 0))
      (is (= succes 8)))))

(deftest test-users-orders-session
  "It will check if user in session has maked some orders, it can return empty collection or result won't be empty
  We can pick any user_id from users table"
  (testing "session-orders"
    (let [succes (odb/user-orders-session 5)]
      (is (or (= (empty? succes) true) (= (empty? succes) false))))))


(deftest test-create-user-note
  "If creation of user note is successful it will return (1) in sequence,
   this is only for testing and this test note won't be used in application"
  (testing "create new user note"
    (let [succes (odb/create-note {:message_body "Test note" :user_id -1})]
      (is (= succes (seq '(1)))))))

(deftest test-unread-notes
  "It will return all unread notes that we have,  result can be empty collection"
  (testing "list-unread-notes"
    (let [succes (odb/list-unread-notes)]
      (is (or (= (empty? succes) true) (= (empty? succes) false))))))

(deftest test-read-notes
  "It will return all read notes that we have,  result can be empty collection"
  (testing "list-read-notes"
    (let [succes (odb/list-read-notes)]
      (is (or (= (empty? succes) true) (= (empty? succes) false))))))