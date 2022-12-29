(ns new-application.test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [new-application.pages :as p]
            [new-application.core :as c]
            [ring.util.request :as req]
            [new-application.administrator :as a]
    ; [midje.sweet :refer [=> facts]]
            ))

(deftest test-main-route
  "testiram da li mi vraca glavnu stranicu sa /"
  (testing "main route"
    (let [response (c/app-routes (mock/request :get "/"))]
      (is (= 200 (:status response))))))

(deftest test-info-route
  "testiram da li mi vraca stranicu sa informacijama"
  (testing "info route"
    (let [response (c/app-routes (mock/request :get "/grujicagro-info"))]
      (is (= 200 (:status response))))))

(deftest test-admin-login-route
  "testiram da li mi vraca stranicu za logovanje admina"
  (testing "admin route"
    (let [response (c/app-routes (mock/request :get "/admin/login"))]
      (is (= 200 (:status response))))))

(deftest test-user-login-route
  "testiram da li mi vraca stranicu za logovanje usera"
  (testing "user route"
    (let [response (c/app-routes (mock/request :get "/user/login"))]
      (is (= 200 (:status response))))))

(deftest test-admin-login
  "testiram da li dobro loguje usera"
  (testing "admin login"
    (let [succes (a/check-credentials {:login "admin" :password "admin"})]
      (is (= succes true)))))
