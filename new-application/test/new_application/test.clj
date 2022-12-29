(ns new-application.test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [new-application.pages :as p]
            [new-application.core :as c]
            [ring.util.request :as req]
            [new-application.administrator :as a]
    ; [midje.sweet :refer [=> facts]]
            [new-application.orderers-db :as odb]))

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
; these two tests for creating users should be started only one time, and only if we don't have test user created already


(deftest test-check-credentials
  "If user credential are true then it will return map of that information"
  (testing "create user"
    (let [succes (odb/check-credentials {:phone "060-0000-000" :password "test"})]
      (is (= succes {:phone "060-0000-000" :password "test"})))))

(deftest test-get-next-id
  "Get next id should return some integer number greater than 0"
  (testing "next-id"
    (let [next (odb/get-next-user-id)]
      (is (= (integer? (odb/get-next-user-id)) true))
      (is (> next 0)))))
