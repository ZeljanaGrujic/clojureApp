(ns new-application.core
  (:require
    [ring.adapter.jetty :as ring]
    [compojure.core :refer :all]
    [compojure.route :as route]
    [hiccup.core :as h]
    [hiccup.form :as form]
    [ring.util.response :as resp]
    [ring.util.request :as req]
    [hiccup.page :refer [html5 include-css include-js]]
    [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
    [new-application.pages :as p]
    ;[new-application.db :as db]
    [ring.middleware.session :as session]
    [ring.middleware.params :refer [wrap-params]]
    [ring.middleware.reload :refer [wrap-reload]]
    [ring.middleware.keyword-params :refer [wrap-keyword-params]]
    [ring.middleware.multipart-params :refer [wrap-multipart-params]]
    [new-application.administrator :as a]
    ;[new-application.db-statistic :as dbs]
    [new-application.orderers-db :as odb]
    [new-application.food-orders-db :as fodb]
    [new-application.users-db :as udb]))

;;Resenje jer baca gresku kad cita polja
(defn full-name [full_name]
  ;name in format Nevena+Arsic
  (clojure.string/replace full_name #"\+" " "))
(defn street [street]
  ;name in format Nevena+Arsic
  (clojure.string/replace street #"\+" " "))

(defn full_name-date-city_part-street-delivered-package-name-id [string]
  ;string contains name and phone in this format
  ;name=Nevena+Arsic&phone=0000&__anti-forgery-token=Unbound%3A+%23%27ring.middleware.anti-forgery%2F*anti-forgery-token*
  (let [map {:full_name (full-name (clojure.string/replace (get (clojure.string/split string #"&") 0) "full_name=" ""))
             :do_date   (clojure.string/replace (get (clojure.string/split string #"&") 1) "do_date=" "")
             :city_part (clojure.string/replace (get (clojure.string/split string #"&") 2) "city_part=" "")
             :street    (street (clojure.string/replace (get (clojure.string/split string #"&") 3) "street=" ""))
             :delivered (clojure.string/replace (get (clojure.string/split string #"&") 4) "delivered=" "")
             :package_name    (clojure.string/replace (get (clojure.string/split string #"&") 5) "package_name=" "")
             :id        (clojure.string/replace (get (clojure.string/split string #"&") 6) "id=" "")}] map))

(defn just-full-name [string]
  ;string contains name and phone in this format
  ;name=Nevena+Arsic&phone=0000&__anti-forgery-token=Unbound%3A+%23%27ring.middleware.anti-forgery%2F*anti-forgery-token*
  (let [map {:full_name (full-name (clojure.string/replace (get (clojure.string/split string #"&") 0) "full_name=" ""))}] map))


(defn administrator [string]
  (let [map {:login    (clojure.string/replace (get (clojure.string/split string #"&") 0) "login=" "")
             :password (clojure.string/replace (get (clojure.string/split string #"&") 1) "password=" "")}] map))


(defn register-user [string]
  (let [map {:owner_name    (clojure.string/replace (get (clojure.string/split string #"&") 0) "owner_name=" "")
             :owner_surname (clojure.string/replace (get (clojure.string/split string #"&") 1) "owner_surname=" "")
             :phone (clojure.string/replace (get (clojure.string/split string #"&") 2) "phone=" "")
             :password (clojure.string/replace (get (clojure.string/split string #"&") 3) "password=" "")}] map))

(defn login-user [string]
  (let [map {
             :phone (clojure.string/replace (get (clojure.string/split string #"&") 0) "phone=" "")
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
;;dodala bih background image, zato sam probala view i background css ali ne ide
;;jedino bih mozda morala da pravim bukvalne html stranice kao posebne viewe pa da ih zovem, a to mi deluje kao puno posla
(defn base-page [& body]
  ;basic template for all our pages
  (html5 [:head [:title "KOKODA - GRUJIC"]]
         [:link {:rel         "stylesheet" :href "https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
                 :integrity   "sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65"
                 :crossorigin "anonymous"}]
         [:link {:rel         "stylesheet"
                 :type "txt/css"
                 :href "background.css"
                 }]

         [:body
          [:div {:class "bg"}
          [:div.container
           [:h1 "KOKODA - GRUJIC"]
           [:h2 "Dnevnik klijenata i prodaje jaja"]
           [:nav.navbar.navbar-expand-lg.navbar-light.bd-light
            [:a.navbar-brand {:href "/grujicagro-info"} "Informacije"]
            [:div.navbar-nav.ml-auto
             [:a.nav-item.nav.link {:href "/admin/login"} "Admin-prijava"]
             [:a.nav-item.nav.link {:href "/admin/logout"} "Admin-odjava"]
             [:a.nav-item.nav.link {:href "/user/register/"} "Registruj se"]
             [:a.nav-item.nav.link {:href "/user/login"} "Prijava"]
             [:a.nav-item.nav.link {:href "/user/logout"} "Odjava"]
             ]] [:hr]
           body]]]))

(defn base-page-admin [& body]
  ;basic template for all our pages
  (html5 [:head [:title "KOKODA - GRUJIC"]]
         [:link {:rel         "stylesheet" :href "https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
                 :integrity   "sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65"
                 :crossorigin "anonymous"}]
         [:link {:rel         "stylesheet"
                 :type "txt/css"
                 :href "background.css"
                 }]

         [:body
          [:div {:class "bg"}
           [:div.container
            [:h1 "KOKODA - GRUJIC"]
            [:h2 "Dnevnik klijenata i prodaje jaja"]
            [:nav.navbar.navbar-expand-lg.navbar-light.bd-light
             [:a.navbar-brand {:href "/grujicagro-info"} "Informacije"]
             [:div.navbar-nav.ml-auto
              [:a.nav-item.nav.link {:href "/admin/login"} "Admin-prijava"]
              [:a.nav-item.nav.link {:href "/admin/logout"} "Admin-odjava"]
              ]] [:hr]
            [:a {:href "/page-orders"} [:h3 "Porucivanje jaja"]]
            [:a {:href "/food-orders"} [:h3 "Porucivanje hrane"]]
            body]]]))


(defn base-page-user [& body]
  ;basic template for all our pages
  (html5 [:head [:title "KOKODA - GRUJIC"]]
         [:link {:rel         "stylesheet" :href "https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
                 :integrity   "sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65"
                 :crossorigin "anonymous"}]
         [:link {:rel         "stylesheet"
                 :type "txt/css"
                 :href "background.css"
                 }]

         [:body
          [:div {:class "bg"}
           [:div.container
            [:h1 "KOKODA - GRUJIC"]
            [:h2 "Dnevnik klijenata i prodaje jaja"]
            [:nav.navbar.navbar-expand-lg.navbar-light.bd-light
             [:a.navbar-brand {:href "/grujicagro-info"} "Informacije"]
             [:div.navbar-nav.ml-auto
              [:a.nav-item.nav.link {:href "/user/register/"} "Registruj se"]
              [:a.nav-item.nav.link {:href "/user/login"} "Prijava"]
              [:a.nav-item.nav.link {:href "/user/logout"} "Odjava"]
              ]] [:hr]
            [:a {:href "/orders/new/"} [:h3 "Porucivanje jaja"]]
            body]]]))



(defroutes app-routes

           (GET "/admin/login" [:as {session :session}]
             ; if admin is already logged in then go to index page
             (if (:admin session)
               (resp/redirect "/admin/home")
               (p/administrator-login)))

           (POST "/admin/login" req
             (let [administrator (administrator (slurp (:body req)))]
               (if (a/check-credentials administrator)
                 (-> (resp/redirect "/admin/home")
                     (assoc-in [:session :admin] true))     ;u http zahtev dodaje se polje :session{:admin true}
                 (p/administrator-login "Neispravno korisnicko ime ili loznka"))))


           (GET "/admin/logout" []
             (-> (resp/redirect "/")
                 (assoc-in [:session :admin] false)))


           ;;ZA REGISTRACIJU I LOGIN USERA
           (GET "/user/register/" [] (p/user-register))
           (POST "/user/register/:id" req (do (let [user (register-user (slurp (:body req)))]
                                                (udb/create-user user))
                                              (resp/redirect "/user/login")))
           (GET "/user/login" [:as {session :session}]
             ; if admin is already logged in then go to index page
             (if (:user session)
               (resp/redirect "/user/home")
               (p/user-login)))

           (POST "/user/login" req
             (let [user (login-user (slurp (:body req)))]
               (if (udb/check-credentials user)
                 (-> (resp/redirect "/user/home")
                     (assoc-in [:session :user] true))     ;u http zahtev dodaje se polje :session{:admin true}
                 (p/administrator-login "Neispravno korisnicko ime ili loznka"))))

           (GET "/user/logout" []
             (-> (resp/redirect "/")
                 (assoc-in [:session :user] false)))



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

           (GET "/admin/home" [] (base-page-admin))
           (GET "/page-orders" [] (p/base-orders-page))
           (GET "/all-orders" [] (p/index (odb/list-orders)))
           (GET "/orders/:order-id" [order-id] (p/view-order (odb/get-order-by-id (read-string order-id))))


           ;(GET "/order/:id" [id] (p/order-view (db/get-order-by-id (read-string id))))


           ;(GET "/orders/new/" [] (p/form-new-order))
           ;(POST "/orders/new/:id" req (do (let [order (full_name-date-city_part-street-delivered-package-name-id (slurp (:body req)))]
           ;                                  (odb/new-order order))
           ;                                (resp/redirect "/page-orders")))


           (GET "/all-orders/update" [] (p/index-for-update (odb/list-orders)))
           (GET "/orders/new/edit/:id" [id] (p/edit-order (odb/get-order-by-id (read-string id))))
           (POST "/orders/:id" req (do (let [order (full_name-date-city_part-street-delivered-package-name-id (slurp (:body req)))]
                                         (odb/edit-order order))
                                       (resp/redirect "/page-orders")))



           (GET "/all-orders/delete" [] (p/index-for-delete (odb/list-orders)))
           (GET "/orders/new/delete/:id" [id] (p/form-delete-order (odb/get-order-by-id (read-string id))))
           (POST "/orders/delete/:id" req (do (let [order (full_name-date-city_part-street-delivered-package-name-id (slurp (:body req)))]
                                                (odb/delete-order order))
                                              (resp/redirect "/page-orders")))
           (GET "/undelivered-orders" [] (p/index-for-undelivered-orders))
           (GET "/undelivered-order/:cp" [cp] (p/orders-view (odb/undelivered-cp cp)))

           (GET "/orders-statistic" [] (p/base-statistic-page))

           ;;ROUTES FOR FOOD ORDERS
           (GET "/food-orders" [] (p/base-food-page))
           (GET "/food-order/new/" [] (p/form-new-food))
           (POST "/food-order/new/:id" req (do (let [food (food-do-date-month-type-amount-id (slurp (:body req)))]
                                             (fodb/new-food-order food))
                                           (resp/redirect "/food-orders")))
           (GET "/all-food-orders" [] (p/index-for-monthly-orders))
           (GET "/month-order/:mo" [mo] (p/food-orders-view (fodb/list-full-forders mo)))

           (GET "/all-food-orders/delete" [] (p/index-for-food-delete (fodb/list-full-forders-delete)))
           (GET "/food-order/delete/:id" [id] (p/form-delete-food-order (fodb/get-food-order-by-id (read-string id))))
           (POST "/food-order/delete/:id" req (do (let [forder (delete-food-do-date-month-type-id (slurp (:body req)))]
                                                (fodb/delete-food-order forder))
                                              (resp/redirect "/food-orders")))
           (GET "/foods-statistic" [] (p/base-food-statistic-page))


           (GET "/orders-search" [] (p/all-orderers (odb/list-orderers-names)))
            (GET "/orderer/:name" [name] (p/orders-view (odb/list-orders-by-name name)))
           ;htela sam da mi se kuca u polju ime, pa da se otvara stranica ali nisam znala da uradim, pa sam uradila preko linkova
           ;(POST "/orders/search/:name" req (do (let [order (just-full-name (slurp (:body req)))]
           ;                                       (odb/list-orders-by-name order)))
           ;                                (resp/redirect "/page-orders"))


           (GET "/food-orders/search" [] (p/all-food-types (fodb/list-type-names)))
           (GET "/food-type/:name" [name] (p/food-orders-view2 (fodb/list-full-forders-by-name name)))



           )

(defroutes user-routes

           (GET "/user/home" [] (base-page-user))
           (GET "/orders/new/" [] (p/form-new-order))
           (POST "/orders/new/:id" req (do (let [order (full_name-date-city_part-street-delivered-package-name-id (slurp (:body req)))]
                                             (odb/new-order order))
                                           (resp/redirect "//user/home")))
           )

;handler je funkcija koja prima zahtev i vraca odgovor
;ako imamo admina u sesiji, koji se prijavio moze, nastavlja dalje rad
;ako ne uloguje admin vraca se na login stranicu ponovo, tu treba da dodam poruku koja ce da mu ispise da nije dobro uneo kredencijale
(defn wrap-admin-only [handler]
  (fn [req]
    (if (-> req :session :admin)
      (handler req)
      (resp/redirect "/admin/login"))))
(defn wrap-user-only [handler]
  (fn [req]
    (if (-> req :session :user)
      (handler req)
      (resp/redirect "/user/login"))))

(def wrapping
  (-> (routes (wrap-routes administrator-routes wrap-admin-only)
              (wrap-routes user-routes wrap-user-only)
              app-routes)
      wrap-multipart-params
      session/wrap-session))

(def server
  (ring/run-jetty wrapping {:port 3030 :join? false}))



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
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World! Your application is started on port 3029 :)"))
;aplikaciju pokrecem iz cmd a pomocu komande lein run
