(ns new-application.core
  (:require
    [ring.adapter.jetty :as ring]
    [compojure.core :refer :all]
    [compojure.route :as route]
    [hiccup.core :as h]
    [hiccup.form :as form]
    [ring.util.response :as resp]
    [ring.util.request :as req]
    [hiccup.page :refer [html5]]
    [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
    [new-application.pages :as p]
    [new-application.db :as db]
    [ring.middleware.session :as session]
    [ring.middleware.params :refer [wrap-params]]
    [ring.middleware.reload :refer [wrap-reload]]
    [ring.middleware.keyword-params :refer [wrap-keyword-params]]
    [ring.middleware.multipart-params :refer [wrap-multipart-params]]
    [new-application.administrator :as a]
    [new-application.db-statistic :as dbs]))

;;Resenje jer baca gresku kad cita polja
(defn full-name [full_name]
  ;name in format Nevena+Arsic
  (clojure.string/replace full_name #"\+" " "))
(defn street [street]
  ;name in format Nevena+Arsic
  (clojure.string/replace street #"\+" " "))

(defn full_name-amount-date-city_part-street-delivered-id [string]
  ;string contains name and phone in this format
  ;name=Nevena+Arsic&phone=0000&__anti-forgery-token=Unbound%3A+%23%27ring.middleware.anti-forgery%2F*anti-forgery-token*
  (let [map {:full_name (full-name (clojure.string/replace (get (clojure.string/split string #"&") 0) "full_name=" ""))
             :amount    (clojure.string/replace (get (clojure.string/split string #"&") 1) "amount=" "")
             :do_date   (clojure.string/replace (get (clojure.string/split string #"&") 2) "do_date=" "")
             :city_part (clojure.string/replace (get (clojure.string/split string #"&") 3) "city_part=" "")
             :street    (street (clojure.string/replace (get (clojure.string/split string #"&") 4) "street=" ""))
             :delivered (clojure.string/replace (get (clojure.string/split string #"&") 5) "delivered=" "")
             :id        (clojure.string/replace (get (clojure.string/split string #"&") 6) "id=" "")}] map))

(defn administrator [string]
  (let [map {:login    (clojure.string/replace (get (clojure.string/split string #"&") 0) "login=" "")
             :password (clojure.string/replace (get (clojure.string/split string #"&") 1) "password=" "")}] map))


(defn food-do-date-month-type-amount-id [string]
  ;string contains name and phone in this format
  ;name=Nevena+Arsic&phone=0000&__anti-forgery-token=Unbound%3A+%23%27ring.middleware.anti-forgery%2F*anti-forgery-token*
  (let [map {:do_date   (clojure.string/replace (get (clojure.string/split string #"&") 0) "do_date=" "")
             :month_name (clojure.string/replace (get (clojure.string/split string #"&") 1) "month_name=" "")
             :type_name    (street (clojure.string/replace (get (clojure.string/split string #"&") 2) "type_name=" ""))
             :amount    (clojure.string/replace (get (clojure.string/split string #"&") 3) "amount=" "")
             :id        (clojure.string/replace (get (clojure.string/split string #"&") 4) "id=" "")}] map))

(defn delete-food-do-date-month-type-id [string]
  ;string contains name and phone in this format
  ;name=Nevena+Arsic&phone=0000&__anti-forgery-token=Unbound%3A+%23%27ring.middleware.anti-forgery%2F*anti-forgery-token*
  (let [map {:do_date   (clojure.string/replace (get (clojure.string/split string #"&") 0) "do_date=" "")
             :month_name (clojure.string/replace (get (clojure.string/split string #"&") 1) "month_name=" "")
             :type_id    (street (clojure.string/replace (get (clojure.string/split string #"&") 2) "type_id=" ""))
             :id        (clojure.string/replace (get (clojure.string/split string #"&") 3) "id=" "")}] map))

;;RUTE

(defn base-page [& body]
  ;basic template for all our pages
  (html5 [:head [:title "KOKODA - GRUJIC"]]
         [:link {:rel         "stylesheet" :href "https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
                 :integrity   "sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65"
                 :crossorigin "anonymous"}]
         [:body
          [:div.container
           [:h1 "KOKODA - GRUJIC"]
           [:h2 "Dnevnik klijenata i prodaje jaja"]
           [:nav.navbar.navbar-expand-lg.navbar-light.bd-light
            [:a.navbar-brand {:href "/grujicagro-info"} "Informacije"]
            [:div.navbar-nav.ml-auto
             [:a.nav-item.nav.link {:href "/admin/login"} "     Prijava           "]
             [:a.nav-item.nav.link  ""]
             ;[:a.nav-item.nav.link {:href "/page-orders"} "Porudzbine"]
             ; [:a.nav-item.nav.link {:href "/orders/new/"} "   Nova porudzbina   "]
             ;[:a.nav-item.nav.link {:href "/all-orders/update"} "   Izmeni porudzbinu   "]
             ; [:a.nav-item.nav.link {:href "/all-orders/delete"} "   Izbrisi porudzbinu    "]
             [:a.nav-item.nav.link {:href "/admin/logout"} "        Odjava        "]]] [:hr]
           [:a {:href "/page-orders"} [:h3 "Porucivanje jaja"]]
           [:a {:href "/food-orders"} [:h3 "Porucivanje hrane"]]
           body]]))



(base-page)


(defroutes app-routes

           (GET "/admin/login" [:as {session :session}]
             ; if admin is already logged in then go to index page
             (if (:admin session)
               (resp/redirect "/")
               (p/administrator-login)))

           (POST "/admin/login" req
             (let [administrator (administrator (slurp (:body req)))]
               (if (a/check-credentials administrator)
                 (-> (resp/redirect "/")
                     (assoc-in [:session :admin] true))     ;u http zahtev dodaje se polje :session{:admin true}
                 (p/administrator-login))))


           (GET "/admin/logout" []
             (-> (resp/redirect "/")
                 (assoc-in [:session :admin] false)))



           (GET "/" [] (base-page))


           (GET "/grujicagro-info" [] (html5 [:p "Napisati neki malo uvod i istoriju firme, ovde bi trebalo dodati malo neke slike"]))
           ;(GET "/all-orders" [] (p/orders-view (db/list-orders)))
           ;(GET "/all-orders" [] (p/index (db/list-orders)))
           ;(GET "/orders/:order-id" [order-id] (p/view-order (db/get-order-by-id (read-string order-id))))
           ;
           ;
           ;;(GET "/order/:id" [id] (p/order-view (db/get-order-by-id (read-string id))))
           ;
           ;
           ;(GET "/orders/new/" [] (p/form-new-order))
           ;(POST "/orders/new/:id" req (do (let [order (full_name-amount-date-city_part-street-delivered-id (slurp (:body req)))]
           ;                                  (db/new-order order))
           ;                                (resp/redirect "/")))
           ;
           ;
           ;(GET "/all-orders/update" [] (p/index-for-update (db/list-orders)))
           ;(GET "/orders/new/edit/:id" [id] (p/edit-order (db/get-order-by-id (read-string id))))
           ;(POST "/orders/:id" req (do (let [order (full_name-amount-date-city_part-street-delivered-id (slurp (:body req)))]
           ;                              (db/edit-order order))
           ;                            (resp/redirect "/")))
           ;
           ;
           ;
           ;(GET "/all-orders/delete" [] (p/index-for-delete (db/list-orders)))
           ;(GET "/orders/new/delete/:id" [id] (p/form-delete-order (db/get-order-by-id (read-string id))))
           ;(POST "/orders/delete/:id" req (do (let [order (full_name-amount-date-city_part-street-delivered-id (slurp (:body req)))]
           ;                                     (db/delete-order order))
           ;                                   (resp/redirect "/")))

           ;(POST "/orders/new/:id" req (do (let [order (full_name-amount-date-city_part-street-delivered-id (slurp (:body req)))]
           ;                                  (db/new-order order))
           ;                                (resp/redirect "/")))

           ;(GET "/order-edit/:id" [id]
           ;  (str (let [order (db/get-order-by-id (read-string id))] (p/edit-order order))))



           ;(POST "/order-edit/:id" req (do (let [order (full_name-amount-date-city_part-street-delivered-id (slurp (:body req)))]
           ;                                  (db/edit-order order))
           ;                                (resp/redirect "/")))


           ;(GET "/order-delete/:id" [id]
           ;  (str (let [order (db/get-order-by-id (read-string id))] (p/form-delete-order order))))

           ;
           ;
           ;(POST "/order-delete/:id" req (do (let [order (full_name-amount-date-city_part-street-delivered-id (slurp (:body req)))]
           ;                                    (db/delete-order order))
           ;                                  (resp/redirect "/")))

           (route/not-found "Not Found")
           )


(defroutes administrator-routes

           (GET "/page-orders" [] (p/base-orders-page))
           (GET "/all-orders" [] (p/index (db/list-delivered-orders)))
           (GET "/orders/:order-id" [order-id] (p/view-order (db/get-order-by-id (read-string order-id))))


           ;(GET "/order/:id" [id] (p/order-view (db/get-order-by-id (read-string id))))


           (GET "/orders/new/" [] (p/form-new-order))
           (POST "/orders/new/:id" req (do (let [order (full_name-amount-date-city_part-street-delivered-id (slurp (:body req)))]
                                             (db/new-order order))
                                           (resp/redirect "/page-orders")))


           (GET "/all-orders/update" [] (p/index-for-update (db/list-orders)))
           (GET "/orders/new/edit/:id" [id] (p/edit-order (db/get-order-by-id (read-string id))))
           (POST "/orders/:id" req (do (let [order (full_name-amount-date-city_part-street-delivered-id (slurp (:body req)))]
                                         (db/edit-order order))
                                       (resp/redirect "/page-orders")))



           (GET "/all-orders/delete" [] (p/index-for-delete (db/list-orders)))
           (GET "/orders/new/delete/:id" [id] (p/form-delete-order (db/get-order-by-id (read-string id))))
           (POST "/orders/delete/:id" req (do (let [order (full_name-amount-date-city_part-street-delivered-id (slurp (:body req)))]
                                                (db/delete-order order))
                                              (resp/redirect "/page-orders")))
           (GET "/undelivered-orders" [] (p/index-for-undelivered-orders))
           (GET "/undelivered-order/:cp" [cp] (p/orders-view (dbs/undelivered-cp cp)))

           (GET "/orders-statistic" [] (p/base-statistic-page))

           ;;ROUTES FOR FOOD ORDERS
           (GET "/food-orders" [] (p/base-food-page))
           (GET "/food-order/new/" [] (p/form-new-food))
           (POST "/food-order/new/:id" req (do (let [food (food-do-date-month-type-amount-id (slurp (:body req)))]
                                             (db/new-food-order food))
                                           (resp/redirect "/food-orders")))
           (GET "/all-food-orders" [] (p/index-for-monthly-orders))
           (GET "/month-order/:mo" [mo] (p/food-orders-view (db/list-full-forders mo)))

           (GET "/all-food-orders/delete" [] (p/index-for-food-delete (db/list-full-forders-delete)))
           (GET "/food-order/delete/:id" [id] (p/form-delete-food-order (db/get-food-order-by-id (read-string id))))
           (POST "/food-order/delete/:id" req (do (let [forder (delete-food-do-date-month-type-id (slurp (:body req)))]
                                                (db/delete-food-order forder))
                                              (resp/redirect "/food-orders")))
           (GET "/foods-statistic" [] (p/base-food-statistic-page))
           )

;handler je funkcija koja prima zahtev i vraca odgovor
;ako imamo admina u sesiji, koji se prijavio moze, nastavlja dalje rad
;ako ne uloguje admin vraca se na login stranicu ponovo, tu treba da dodam poruku koja ce da mu ispise da nije dobro uneo kredencijale
(defn wrap-admin-only [handler]
  (fn [req]
    (if (-> req :session :admin)
      (handler req)
      (resp/redirect "/admin/login"))))
(def wrapping
  (-> (routes (wrap-routes administrator-routes wrap-admin-only)
              app-routes)
      wrap-multipart-params
      session/wrap-session))

(def server
  (ring/run-jetty wrapping {:port 3027 :join? false}))



;(defn full_name-amount-date-id [string]
;  ;string contains name and phone in this format
;  ;name=Nevena+Arsic&phone=0000&__anti-forgery-token=Unbound%3A+%23%27ring.middleware.anti-forgery%2F*anti-forgery-token*
;  (let [map {:full_name  (full-name (clojure.string/replace (get (clojure.string/split string #"&") 0) "full_name=" ""))
;             :amount (clojure.string/replace (get (clojure.string/split string #"&") 1) "amount=" "")
;             :do_date (clojure.string/replace (get (clojure.string/split string #"&") 2) "do_date=" "")
;             :id (clojure.string/replace (get (clojure.string/split string #"&") 3) "id=" "")}] map))
;
;(defn full_name-amount-date-location-delivered-id [string]
;  ;string contains name and phone in this format
;  ;name=Nevena+Arsic&phone=0000&__anti-forgery-token=Unbound%3A+%23%27ring.middleware.anti-forgery%2F*anti-forgery-token*
;  (let [map {:full_name  (full-name (clojure.string/replace (get (clojure.string/split string #"&") 0) "full_name=" ""))
;             :amount (clojure.string/replace (get (clojure.string/split string #"&") 1) "amount=" "")
;             :do_date (clojure.string/replace (get (clojure.string/split string #"&") 2) "do_date=" "")
;             :location (clojure.string/replace (get (clojure.string/split string #"&") 3) "location=" "")
;             :delivered (clojure.string/replace (get (clojure.string/split string #"&") 4) "delivered=" "")
;             :id (clojure.string/replace (get (clojure.string/split string #"&") 5) "id=" "")}] map))



;(def app
;  (-> app-routes (wrap-defaults  site-defaults)
;                session/wrap-session))


;;pokrecem aplikaciju sa lein run i na portu 3020 ce mi se pojaviti aplikacija

;;main sam dodala da bi mi se pokrenula aplikacija
