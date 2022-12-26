(ns new-application.pages
  (:require
    [hiccup.page :refer [html5]]
    [hiccup.form :as form]
    [ring.util.anti-forgery :refer (anti-forgery-field)]
    [new-application.db :as db]
    [new-application.db-statistic :as dbs]))



(defn base-orders-page [& body]
  (html5 [:head [:title "KOKODA - GRUJIC"]]
         [:link {:rel         "stylesheet"
                 :href "https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
                 :integrity   "sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65"
                 :crossorigin "anonymous"}]
         [:body
          [:div.container
           [:h2 "Dnevnik klijenata i prodaje jaja"]
           [:nav.navbar.navbar-expand-lg.navbar-light.bd-light
            [:a.navbar-brand {:href "/"} "      Pocetna stranica      "]
            [:a.nav-item.nav.link {:href "/admin/logout"} "        Odjava        "]
            ;[:div.navbar-nav.ml-auto
            ; [:a.nav-item.nav.link {:href "/all-orders"} "Porudzbine"]]
            ;[:div.navbar-nav.ml-auto
            ; [:a.nav-item.nav.link {:href "/orders/new/"} "Kreiraj novu" "\t"]]
            ;[:div.navbar-nav.ml-auto
            ; [:a.nav-item.nav.link {:href "/all-orders/update"} "Izmeni"]]
            ;[:div.navbar-nav.ml-auto
            ; [:a.nav-item.nav.link {:href "/all-orders/delete"} "Obrisi"]]]
            ][:hr]
           [:a {:href "/all-orders"} [:h3 "Isporucene porudzbine"]]
           [:a {:href "/undelivered-orders"} [:h3 "Neisporucene porudzbine"]]
           [:a {:href "/orders/new/"} [:h3 "Kreiraj novu porudzbinu"]]
           [:a {:href "/all-orders/update"} [:h3 "Izmeni porudzbinu"]]
           [:a {:href "/all-orders/delete"} [:h3 "Obrisi porudzbinu"]]
           [:a {:href "/orders-statistic"} [:h3 "Statisticki izvestaj"]]
           body]]))

(defn base-food-page [& body]
  (html5 [:head [:title "KOKODA - GRUJIC"]]
         [:link {:rel         "stylesheet"
                 :href "https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
                 :integrity   "sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65"
                 :crossorigin "anonymous"}]
         [:body
          [:div.container
           [:h2 "Dnevnik porucivanja hrane za koke"]
           [:nav.navbar.navbar-expand-lg.navbar-light.bd-light
            [:a.navbar-brand {:href "/"} "      Pocetna stranica      "]
            [:a.nav-item.nav.link {:href "/admin/logout"} "        Odjava        "]
            ;[:div.navbar-nav.ml-auto
            ; [:a.nav-item.nav.link {:href "/all-orders"} "Porudzbine"]]
            ;[:div.navbar-nav.ml-auto
            ; [:a.nav-item.nav.link {:href "/orders/new/"} "Kreiraj novu" "\t"]]
            ;[:div.navbar-nav.ml-auto
            ; [:a.nav-item.nav.link {:href "/all-orders/update"} "Izmeni"]]
            ;[:div.navbar-nav.ml-auto
            ; [:a.nav-item.nav.link {:href "/all-orders/delete"} "Obrisi"]]]
            ][:hr]
           [:a {:href "/all-food-orders"} [:h3 "Porudzbine hrane"]]
           [:a {:href "/food-order/new/"} [:h3 "Kreiraj novu porudzbinu"]]
           [:a {:href "/all-food-orders/delete"} [:h3 "Obrisi porudzbinu"]]
           [:a {:href "/foods-statistic"} [:h3 "Statisticki izvestaj"]]
           body]]))



(defn base [& body]
  (html5
    [:head [:title "GRUJIC- agro"]]
    [:link {:rel         "stylesheet" :href "https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
            :integrity   "sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65"
            :crossorigin "anonymous"}]
    [:body

     [:div.container
      [:h1 "KOKODA - GRUJIC"]
      [:h2 "Dnevnik klijenata i prodaje jaja"]
      [:nav.navbar.navbar-expand-lg.navbar-light.bd-light
       [:a.navbar-brand {:href "/"} "Pocetna stranica"]
       ;mozda dodati i da mi se izlista za dostavu hrane, kad i koliko, i za mesec
       ;ali pamtiti foru za svakog meseca tonu i fiksirati cenu tone i onda samo za taj isti mesec po nekoliko puta dodavati narudzbine pa onda prebrojati sve to
       [:div.navbar-nav.ml-right
        [:a.nav-item.nav.link {:href "/admin/logout"} "Odjava"]] [:hr]] body]]))

(defn administrator-login []
  (html5
    [:body
     (form/form-to
       [:post (str "/admin/login")]

       (form/label "login" "Login:")
       (form/text-field "login" "login")
       (form/label "password" "Password:")
       (form/password-field "password" "password")

       (anti-forgery-field)
       (form/submit-button "Login")
       )]))

;(defn base-page [& body]
;  ;basic template for all our pages
;  (html [:head [:title "New user"]]
;         [:body [:a {:href "/"}[:h1 "Baza znanja"]]
;          body]))

;(defn index [body]
;  (base body))

(defn index [orders]
  (base [:h1 "All Orders"]
        (for [o orders]
          [:h4 [:a {:href (str "/orders/" (:id o))} "ID porudzbine: " (:id o) "         Porucilac: " (:full_name o) "               Datum isporuke: " (:do_date o)]])))

(defn index-for-update [orders]
  (base [:h1 "Choose order to update"]
        (for [o orders]
          [:h4 [:a {:href (str "/orders/new/edit/" (:id o))} "ID porudzbine: " (:id o) "         Porucilac:  " (:full_name o) "                Datum isporuke: " (:do_date o)]])))

(defn index-for-delete [orders]
  (base [:h1 "Choose order to delete"]
        (for [o orders]
          [:h4 [:a {:href (str "/orders/new/delete/" (:id o))} "ID porudzbine: " (:id o) "         Porucilac:  " (:full_name o) "              Datum isporuke: " (:do_date o)]])))


(defn index-for-undelivered-orders []
  (base [:h1 "City parts for undelivered orders:"]
        (for [cp ["Kolonija" "Centar" "HRS" "HIM", "VUK", "Naselje", "Mikulja", "Bolnica", "Gimnazija", "Opeka", "Jezero" "Suleiceva" "Zelengora" "GOSA" "Kiseljak"]]
          [:h4 [:a {:href (str "/undelivered-order/"cp)} "Deo grada: " cp]])
        ))


(defn index-for-monthly-orders []
  (base [:h1 "Orders per month:"]
        (for [mo ["Januar" "Februar" "Mart" "April", "Maj", "Jun", "Jul", "Avgust", "Septembar", "Oktobar", "Novembar" "Decembar"]]
          [:h4 [:a {:href (str "/month-order/"mo)} "Mesec: "mo]])
        ))

(defn index-for-food-delete [forders]
  (base [:h1 "Choose order to delete"]
        (for [o forders]
          [:h4 [:a {:href (str "/food-order/delete/" (:id o))} "ID porudzbine: " (:id o) "        Datum isporuke: " (:do_date o) "            Mesec: " (:month_name o) "        Vrsta hrane: " (:type_name o)]])))


(index-for-undelivered-orders)

(defn view-order [order]
  (base
    [:a {:href (str "/orders/new/edit/" (:id order))} "Edit order directly"]
    [:hr]
    [:small (:id order) " Redni br. narudzbine"]
    [:h2 "Narucilac: " (:full_name order) "           Porucena kolicina: " (:amount order)]
    [:h2 "Deo grada:  " (:city_part order) "            Ulica i broj: " (:street order)]
    [:h2 "Datum isporuke: " (:do_date order) "            Izvrsena isporuka:  " (:delivered order)]))

(view-order {:id        1,
             :full_name "Srecko Grujic",
             :amount    "0",
             :do_date   "00.00.0000",
             :city_part "Naselje",
             :street    "Dr. Cambe 10",
             :delivered "DA"})

(index (db/list-orders))
(view-order (db/get-order-by-id 1))


(defn order-view [{id :id full_name :full_name amount :amount do_date :do_date city_part :city_part street :street}]
  (html5
    [:li (format " Order id: %s      Full_name: %s        Amount: %s        Do_date: %s     City part: %s   Street: %s    " id full_name amount do_date city_part street)]))

(defn orders-view [orders]
  (html5 [:ul
          (map  order-view orders)]))


(orders-view (dbs/undelivered-cp "Centar"))

(defn num-order-per-person [{full_name :full_name maked_orders :maked_orders}]
  (html5
    [:li (format " Full name: %s            Number of maked orders: %s" full_name maked_orders)]))

(num-order-per-person {:full_name "NECA", :maked_orders 9})

(defn persons-orders [porders]
  (html5 [:ul
          (map  num-order-per-person porders)]))
(dbs/orders-per-person)
(persons-orders (dbs/orders-per-person))




(defn food-order-view [{id :id amount :amount do_date :do_date month_name :month_name type_name :type_name}]
  (html5
    [:li (format " Order id: %s         Amount: %s           Do_date: %s         Type: %s    " id amount do_date type_name)]))

(defn food-orders-view [forders]
  (html5 [:ul
          (map  food-order-view forders)]))



(defn num-order-per-person [{full_name :full_name maked_orders :maked_orders}]
  (html5
    [:li (format " Full name: %s            Number of maked orders: %s" full_name maked_orders)]))

(num-order-per-person {:full_name "NECA", :maked_orders 9})

(defn persons-orders [porders]
  (html5 [:ul
          (map  num-order-per-person porders)]))



(defn total-orders-per-month [{month_name :month_name monthly_orders :monthly_orders total_price :total_price total_amount :total_amount}]
  (html5
    [:li (format " Month: %s            Number of maked orders: %s        Total price: %s           Total amount: %s" month_name monthly_orders total_price total_amount)]))

(defn all-statistic-for-months [opm]
  (html5 [:ul
          (map  total-orders-per-month opm)]))




(defn base-statistic-page []
  (base
    [:h3 "Statistika narudzbina"]
    [:h4 (format "Ukupan broj ostavrenih narudzbina: %s" (dbs/total-num-orders))]
    [:h4 (format "Ukupan broj isporucenih narudzbina: %s" (dbs/total-num-delivered-orders))]
    [:h4 (format "Ukupan broj neisporucenih narudzbina: %s" (dbs/total-num-undelivered-orders))]
    [:hr]
    [:h4 "Broj narudzbina savkog korisnika:"]
    (persons-orders (dbs/orders-per-person))))
(base-statistic-page)


(defn base-food-statistic-page []
  (base
    [:h3 "Statistika porucivanja hrane za koke:"]
    [:h4 (format "Ukupan broj ostavrenih narudzbina: %s" (:total_num_orders (nth (dbs/general-food-statistic) 0)))]
    [:h4 (format "Ukupan ostvareni trosak: %s" (:general_price (nth (dbs/general-food-statistic) 0)))]
    [:h4 (format "Ukupna porucena kolicina: %s" (:general_amount (nth (dbs/general-food-statistic) 0)))]
    [:hr]
    [:h4 "Statistika narudzbina po mesecu:"]
    (all-statistic-for-months (dbs/orders-per-month))))


;;;preko atoma
;(order-view (db/orders-data 1))
;(orders-view db/orders-data)
;
;(order-view (db/get-order-by-id 2))
;(orders-view (db/list-orders))


;(defn one-order-view [order]
;  (html5 [:ul
;          (map (fn [[k v]] [:li (format "%s : %s" (name k) (str v))]) order)]))
;(one-order-view (db/get-order-by-id 2))


(defn form-new-order []
  (html5
    [:body
     (form/form-to [:post (str "/orders/new/" (db/get-next-id))]

                   (form/label "full_name" "Full name: ")
                   (form/text-field "full_name" "ime")
                   [:hr]
                   (form/label "amount" "Amount: ")
                   (form/text-field "amount" "amount")
                   [:hr]
                   (form/label "do_date" "Do date: ")
                   (form/text-field "do_date" "do date")
                   [:hr]
                   (form/label "city_part" "City part: ")
                   ;(form/text-field "location" "Location")
                   [:div.div-separator (form/drop-down {:class "form-class"} "city_part" ["Kolonija" "Centar" "HRS" "HIM", "VUK", "Naselje", "Mikulja", "Bolnica", "Gimnazija", "Opeka", "Jezero" "Suleiceva" "Zelengora" "GOSA" "Kiseljak"])]
                   [:hr]
                   (form/label "street" "Street: ")
                   (form/text-field "street" "street")
                   [:hr]
                   (form/label "delivered" "Delivered (DA/NE): ")
                   ;(form/text-field "delivered" "delivered")
                   [:div.div-separator (form/drop-down {:class "form-class"} "delivered" ["DA" "NE"])]
                   (form/hidden-field "id" (db/get-next-id))
                   (anti-forgery-field)

                   (form/submit-button "Save order"))]))

(defn new-order [order]
  (db/new-order order))

;(new-order {:full_name "GIARDINO" :amount "300" :do_date "23.12.2022" :location "Centar" :delivered "NE"})



(defn edit-order [order]
  (html5
    [:body
     ;[:p "Broj porudzbine koja se menja: " (:id order)]
     (form/form-to [:post (if order
                            (str "/orders/" (:id order))
                            "/orders")]

                   (form/label "full_name" "Full name: ")
                   (form/text-field "full_name" (:full_name order))
                   [:hr]
                   (form/label "amount" "Amount: ")
                   (form/text-field "amount" (:amount order))
                   [:hr]
                   (form/label "do_date" "Do date: ")
                   (form/text-field "do_date" (:do_date order))
                   [:hr]
                   (form/label "city_part" "City part: ")
                   ;(form/text-field "location" (:location order))
                   (form/drop-down {:class "form-class"} "city_part" ["Kolonija" "Centar" "HRS" "HIM", "VUK", "Naselje", "Mikulja", "Bolnica", "Gimnazija", "Opeka", "Jezero" "Suleiceva" "Zelengora" "GOSA" "Kiseljak"])
                   [:hr]
                   (form/label "street" "Street: ")
                   (form/text-field "street" (:street order))
                   [:hr]
                   (form/label "delivered" "Delivered (DA/NE): ")
                   ;(form/text-field "deliverede" (:delivered order))
                   (form/drop-down {:class "form-class"} "delivered" ["DA" "NE"])
                   (form/hidden-field "id" (:id order))
                   (anti-forgery-field)

                   (form/submit-button "Save changes"))]))

(defn form-delete-order [order]
  (html5
    [:body
     [:p (:id order)]
     (form/form-to [:post (if order
                            (str "/orders/delete/" (:id order))
                            "/orders")]

                   (form/label "full_name" "Full name: ")
                   (form/text-field "full_name" (:full_name order))
                   [:hr]
                   (form/label "amount" "Amount: ")
                   (form/text-field "amount" (:amount order))
                   [:hr]
                   (form/label "do_date" "Do date: ")
                   (form/text-field "do_date" (:do_date order))
                   [:hr]
                   (form/label "city_part" "City part: ")
                   (form/text-field "city_part" (:city_part order))
                   [:hr]
                   (form/label "street" "Street: ")
                   (form/text-field "street" (:street order))
                   [:hr]
                   (form/label "delivered" "Delivered (DA/NE): ")
                   (form/text-field "deliverede" (:delivered order))
                   (form/hidden-field "id" (:id order))
                   (anti-forgery-field)

                   (form/submit-button "Delete order"))]))



(defn form-delete-food-order [forder]
  (html5
    [:body
     [:p (:id forder)]
     (form/form-to [:post (if forder
                            (str "/food-order/delete/" (:id forder))
                            "/all-food-orders/delete")]

                   (form/label "do_date" "Do date: ")
                   (form/text-field "do_date" (:do_date forder))
                   [:hr]
                   (form/label "month_name" "Month: ")
                   (form/text-field "month_name" (:month_name forder))
                   [:hr]
                   (form/hidden-field "type_id" (:type_id forder))
                   (form/hidden-field "id" (:id forder))
                   (anti-forgery-field)

                   (form/submit-button "Delete order"))]))


;(edit-order (db/get-order-by-id 2))


(defn form-new-food []
  (html5
    [:body
     (form/form-to [:post (str "/food-order/new/"(db/get-next-food-id))]

                   (form/label "do_date" "Do date: ")
                   (form/text-field "do_date" "do date")
                   [:hr]
                   (form/label "month_name" "Month: ")
                   ;(form/text-field "location" "Location")
                   [:div.div-separator (form/drop-down {:class "form-class"} "month_name" ["Januar" "Februar" "Mart" "April", "Maj", "Jun", "Jul", "Avgust", "Septembar", "Oktobar", "Novembar" "Decembar"])]
                   [:hr]
                   (form/label "type_name" "Vrsta hrane: ")
                   ;(form/text-field "delivered" "delivered")
                   [:div.div-separator (form/drop-down {:class "form-class"} "type_name" ["Pantelic zito" "Pantelic vitamini"])]
                   [:hr]
                   (form/label "amount" " Porucena kolicina - 1000kg (oznacite polje!)")
                   [:div.div-separator (form/check-box {:class "form-class"} "amount" true 1000)]
                   (form/hidden-field "id"(db/get-next-food-id))
                   (anti-forgery-field)

                   (form/submit-button "Save order"))]))



;ova forma ce biti koriscena za kreiranje i editovanje narudzbine
;ali po istom principu moze da se koristi za create/update bilo cega
;ovo cu verovatno ovako da pravim za pitanja


