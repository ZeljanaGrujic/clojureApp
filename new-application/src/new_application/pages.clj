(ns new-application.pages
  (:require
    [hiccup.page :refer [html5]]
    [hiccup.form :as form]
    [ring.util.anti-forgery :refer (anti-forgery-field)]
    ;[new-application.db :as db]
    ;[new-application.db-statistic :as dbs]
    [new-application.orderers-db :as odb]
    [new-application.food-orders-db :as fodb]
    [new-application.users-db :as udb]))


(defn base-page [& body]
  ;basic template for all our pages
  (html5 [:head [:title "KOKODA - GRUJIC"]]
         [:link
          {:rel "stylesheet"
           :href "https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
           :integrity "sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65"
           :crossorigin "anonymous"}]
         [:link {:rel         "stylesheet"
                 :type "txt/css"
                 :href "background.css"}]

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
         [:link
          {:rel "stylesheet"
           :href "https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
           :integrity "sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65"
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


(defn base-page-user [session]
  ;basic template for all our pages
  (html5 [:head [:title "KOKODA - GRUJIC"]]
         [:link
          {:rel "stylesheet"
           :href "https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
           :integrity "sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65"
           :crossorigin "anonymous"}]
         [:link {:rel         "stylesheet"
                 :type "txt/css"
                 :href "background.css"
                 }]

         [:body
          [:div {:class "bg"}
           [:div.container
            [:h1 "KOKODA - GRUJIC"]
            [:nav.navbar.navbar-expand-lg.navbar-light.bd-light
             [:a.navbar-brand {:href "/grujicagro-info"} "Informacije"]
             [:div.navbar-nav.ml-auto
              [:a.nav-item.nav.link {:href "/user/register/"} "Registruj se"]
              [:a.nav-item.nav.link {:href "/user/login"} "Prijava"]
              [:a.nav-item.nav.link {:href "/user/logout"} "Odjava"]
              ]] [:hr]
            [:a {:href "/orders/new/"} [:h3 "Porucivanje jaja"]]
            [:a {:href "/user/account/"} [:h3 "Moj nalog"]]
            ]]]))




(defn base-orders-page [& body]
  (html5 [:head [:title "KOKODA - GRUJIC"]]
         [:link {:rel         "stylesheet"
                 :href        "https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css"
                 :integrity   "sha384-xOolHFLEh07PJGoPkLv1IbcEPTNtaed2xpHsD9ESMhqIYd0nLMwNLD69Npy4HI+N"
                 :crossorigin "anonymous"}]
         [:body
          [:div.container
           [:h2 "Dnevnik klijenata i prodaje jaja"]
           [:nav.navbar.navbar-expand-lg.navbar-light.bd-light
            [:a.navbar-brand {:href "/admin/home"} "      Pocetna stranica      "]
            [:a.nav-item.nav.link {:href "/admin/logout"} "        Odjava        "]
            ;[:div.navbar-nav.ml-auto
            ; [:a.nav-item.nav.link {:href "/all-orders"} "Porudzbine"]]
            ;[:div.navbar-nav.ml-auto
            ; [:a.nav-item.nav.link {:href "/orders/new/"} "Kreiraj novu" "\t"]]
            ;[:div.navbar-nav.ml-auto
            ; [:a.nav-item.nav.link {:href "/all-orders/update"} "Izmeni"]]
            ;[:div.navbar-nav.ml-auto
            ; [:a.nav-item.nav.link {:href "/all-orders/delete"} "Obrisi"]]]
            ] [:hr]
           [:a {:href "/all-orders"} [:h3 "Sve porudzbine"]]
           [:a {:href "/undelivered-orders"} [:h3 "Neisporucene porudzbine"]]
           ;[:a {:href "/orders/new/"} [:h3 "Kreiraj novu porudzbinu"]]
           [:a {:href "/all-orders/update"} [:h3 "Izmeni porudzbinu"]]
           [:a {:href "/all-orders/delete"} [:h3 "Obrisi porudzbinu"]]
           [:a {:href "/orders-search"} [:h3 "Porucioci"]]
           [:a {:href "/orders-statistic"} [:h3 "Statisticki izvestaj"]]
           body]]))

(base-orders-page)

(defn base-food-page [& body]
  (html5 [:head [:title "KOKODA - GRUJIC"]]
         [:link {:rel         "stylesheet"
                 :href        "https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css"
                 :integrity   "sha384-xOolHFLEh07PJGoPkLv1IbcEPTNtaed2xpHsD9ESMhqIYd0nLMwNLD69Npy4HI+N"
                 :crossorigin "anonymous"}]
         [:body
          [:div.container
           [:h2 "Dnevnik porucivanja hrane za koke"]
           [:nav.navbar.navbar-expand-lg.navbar-light.bd-light
            [:a.navbar-brand {:href "/admin/home"} "      Pocetna stranica      "]
            [:a.nav-item.nav.link {:href "/admin/logout"} "        Odjava        "]
            ;[:div.navbar-nav.ml-auto
            ; [:a.nav-item.nav.link {:href "/all-orders"} "Porudzbine"]]
            ;[:div.navbar-nav.ml-auto
            ; [:a.nav-item.nav.link {:href "/orders/new/"} "Kreiraj novu" "\t"]]
            ;[:div.navbar-nav.ml-auto
            ; [:a.nav-item.nav.link {:href "/all-orders/update"} "Izmeni"]]
            ;[:div.navbar-nav.ml-auto
            ; [:a.nav-item.nav.link {:href "/all-orders/delete"} "Obrisi"]]]
            ] [:hr]
           [:a {:href "/all-food-orders"} [:h3 "Porudzbine hrane"]]
           [:a {:href "/food-order/new/"} [:h3 "Kreiraj novu porudzbinu"]]
           [:a {:href "/all-food-orders/delete"} [:h3 "Obrisi porudzbinu"]]
           [:a {:href "/food-orders/search"} [:h3 "Porucena hrana"]]
           [:a {:href "/foods-statistic"} [:h3 "Statisticki izvestaj"]]
           body]]))



(defn base [& body]
  (html5
    [:head [:title "KOKODA - GRUJIC"]]
    [:link {:rel         "stylesheet"
            :href        "https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css"
            :integrity   "sha384-xOolHFLEh07PJGoPkLv1IbcEPTNtaed2xpHsD9ESMhqIYd0nLMwNLD69Npy4HI+N"
            :crossorigin "anonymous"}]
    [:body

     [:div.container
      [:h1 "KOKODA - GRUJIC"]
      [:h2 "Dnevnik klijenata i prodaje jaja"]
      [:nav.navbar.navbar-expand-lg.navbar-light.bd-light
       [:a.navbar-brand {:href "/admin/home"} "Pocetna stranica"]
       ;mozda dodati i da mi se izlista za dostavu hrane, kad i koliko, i za mesec
       ;ali pamtiti foru za svakog meseca tonu i fiksirati cenu tone i onda samo za taj isti mesec po nekoliko puta dodavati narudzbine pa onda prebrojati sve to
       [:div.navbar-nav.ml-right
        [:a.nav-item.nav.link {:href "/admin/logout"} "Odjava"]] [:hr]] body]]))

(defn administrator-login [& [msg]]
  (html5
    (when msg [:div.alert.alert-danger msg])
    [:body
     (form/form-to
       [:post (str "/admin/login")]

       [:div.form-group
        (form/label "login" "Korisnicko ime:  ")
        (form/text-field {:class "form-control"} "login")]
       [:hr]
       [:div.form-group
        (form/label "password" "Lozinka:  ")
        (form/password-field {:class "form-control"} "password")]
       (anti-forgery-field)
       [:hr]
       (form/submit-button {:class "btn btn-primary"} "Uloguj se")
       )]))

;(defn base-page [& body]
;  ;basic template for all our pages
;  (html [:head [:title "New user"]]
;         [:body [:a {:href "/"}[:h1 "Baza znanja"]]
;          body]))

;(defn index [body]
;  (base body))


(defn view-user-account [session]
  (html5
    [:div.container
     [:nav.navbar.navbar-expand-lg.navbar-light.bd-light
      [:a.navbar-brand {:href "/user/home"} "      Pocetna stranica      "]
      [:a.nav-item.nav.link {:href "/user/logout"} "        Odjava        "]]
     [:hr]
     [:small (:id session) " Jedinstveni broj korisnika"]
    [:h2 "Ime: " (odb/get-name-by-id-session (:id session))]
    [:h2 "Prezime:  " (odb/get-surname-by-id-session (:id session))]
    [:h2 "Telefon: " (odb/get-phone-by-id-session (:id session))]]))


(defn index [orders]
  (base [:h1 "Sve porudzbine"]
        (for [o orders]
          [:div
           [:h4 [:a {:href (str "/orders/" (:id o))} "ID porudzbine: " (:id o) "                Porucilac: " (:full_name o) "                      Datum isporuke: " (:do_date o)]]])))


(index (odb/list-delivered-orders))


(defn index-for-update [orders]
  (base [:h1 "Izaberi narudzbinu za izmenu:"]
        (for [o orders]
          [:div [:h4 [:a {:href (str "/orders/new/edit/" (:id o))} "ID porudzbine: " (:id o) "            Porucilac:  " (:full_name o) "                    Datum isporuke: " (:do_date o)]]])))

(defn index-for-delete [orders]
  (base [:h1 "Izaberi narudzbinu za brisanje:"]
        (for [o orders]
          [:div
           [:h4 [:a {:href (str "/orders/new/delete/" (:id o))} "ID porudzbine: " (:id o) "               Porucilac:  " (:full_name o) "                     Datum isporuke: " (:do_date o)]]])))


(defn index-for-undelivered-orders []
  (base [:h1 "Odaberi deo grada u kome zelis da vidis neisporucene narudzbine:"]
        (for [cp ["Kolonija" "Centar" "HRS" "HIM", "VUK", "Naselje", "Mikulja", "Bolnica", "Gimnazija", "Opeka", "Jezero" "Suleiceva" "Zelengora" "GOSA" "Kiseljak"]]
          [:div [:h4 [:a {:href (str "/undelivered-order/" cp)} "Deo grada: " cp]]]
          )))


(defn index-for-monthly-orders []
  (base [:h1 "Narudzbine za mesec:"]
        (for [mo ["Januar" "Februar" "Mart" "April", "Maj", "Jun", "Jul", "Avgust", "Septembar", "Oktobar", "Novembar" "Decembar"]]
          [:div [:h4 [:a {:href (str "/month-order/" mo)} "Mesec: " mo]]]
          )))

(defn index-for-food-delete [forders]
  (base [:h1 "Izaberi narudzbinu za brisanje:"]
        (for [o forders]
          [:div [:h4 [:a {:href (str "/food-order/delete/" (:id o))} "ID porudzbine: " (:id o) "        Datum isporuke: " (:do_date o) "            Mesec: " (:month_name o) "        Vrsta hrane: " (:type_name o)]]]
          )))


(index-for-undelivered-orders)

(defn view-order [order]
  (base
    [:a {:href (str "/orders/new/edit/" (:id order))} "Direktno izmeni porudzbinu"]
    [:hr]
    [:small (:id order) " Redni br. narudzbine"]
    [:h2 "Narucilac: " (:full_name order) "           Porucena kolicina: " (:amount order) "          Cena:" (:price order)]
    [:h2 "Deo grada:  " (:city_part order) "            Ulica i broj: " (:street order)]
    [:h2 "Datum isporuke: " (:do_date order) "            Izvrsena isporuka:  " (:delivered order)]
    [:h2 "Telefon narucioca: " (:phone order) ]))

(view-order {:id        2,
             :full_name "JESA",
             :do_date   "27.12.2022.",
             :city_part "Centar",
             :street    "Ustanicka 5",
             :delivered "NE",
             :amount    270,
             :price     1650.0})

(index (odb/list-orders-all-info))
(view-order (odb/get-order-by-id 1))


(defn order-view [{id :id full_name :full_name do_date :do_date city_part :city_part street :street amount :amount price :price}]
  (html5
    [:li (format " ID porudzbine: %s         Narucilac: %s          Datum isporuke: %s        Deo grada: %s          Ulica: %s         Kolicina: %s                Cena: %s " id full_name do_date city_part street amount price)]))

(defn orders-view [orders]
  (if (= orders ())
    (html5 [:h2 "Nema porudzbina za ovaj deo grada"])
    (html5 [:ul
            (map order-view orders)])))

(defn orders-view2 [& [orders]]
  (when orders (html5 [:ul
                       (map order-view orders)])))



(orders-view (odb/undelivered-cp "Centar"))

(defn num-order-per-person [{full_name :full_name maked_orders :maked_orders total_amount :total_amount total_price :total_price}]
  (html5
    [:li (format " Narucilac: %s            Broj naruzbina: %s       Ukupna kolicina: %s          Ukupna cena: %s dinara" full_name maked_orders total_amount total_price)]))

(num-order-per-person {:full_name "NECA", :maked_orders 9 :total_amount 300 :price 1650})

(defn persons-orders [porders]
  (html5 [:ul
          (map num-order-per-person porders)]))
(odb/orders-per-person)
(persons-orders (odb/orders-per-person))




(defn food-order-view [{id :id amount :amount do_date :do_date month_name :month_name type_name :type_name}]
  (html5
    [:li (format " ID naruzbine: %s         Kolicina: %s           Datum isporuke: %s         Vrsta hrane: %s    " id amount do_date type_name)]))

(defn food-orders-view [forders]
  (if (= forders ())
    (html5 [:h2 "Nema porudzbina za ovaj mesec"])
    (html5 [:ul
            (map food-order-view forders)])))



;(defn num-order-per-person [{full_name :full_name maked_orders :maked_orders}]
;  (html5
;    [:li (format " Full name: %s            Number of maked orders: %s" full_name maked_orders)]))

;(num-order-per-person {:full_name "NECA", :maked_orders 9})
;
;(defn persons-orders [porders]
;  (html5 [:ul
;          (map  num-order-per-person porders)]))



(defn total-orders-per-month [{month_name :month_name monthly_orders :monthly_orders total_price :total_price total_amount :total_amount}]
  (html5
    [:li (format " Mesec: %s            Broj narudzbina: %s        Ukupna cena: %s           Ukupna kolicina: %s" month_name monthly_orders total_price total_amount)]))

(defn all-statistic-for-months [opm]
  (if (= opm ())
    (html5 [:h2 "Nema porudzbina za ovaj mesec"])
    (html5 [:ul
            (map total-orders-per-month opm)])))
(all-statistic-for-months (fodb/list-full-forders "April"))




(defn base-statistic-page []
  (base
    [:h3 "Statistika narudzbina"]
    [:h4 (format "Ukupan broj ostavrenih narudzbina: %s" (odb/total-num-orders))]
    [:h4 (format "Ukupan broj isporucenih narudzbina: %s" (odb/total-num-delivered-orders))]
    [:h4 (format "Ukupan broj neisporucenih narudzbina: %s" (odb/total-num-undelivered-orders))]
    [:hr]
    [:h4 "Broj narudzbina savkog korisnika:"]
    (persons-orders (odb/orders-per-person))))
(base-statistic-page)


(defn base-food-statistic-page []
  (base
    [:h3 "Statistika porucivanja hrane za koke:"]
    [:h4 (format "Ukupan broj ostavrenih narudzbina: %s" (:total_num_orders (nth (fodb/general-food-statistic) 0)))]
    [:h4 (format "Ukupan ostvareni trosak : %s evra" (:general_price (nth (fodb/general-food-statistic) 0)))]
    [:h4 (format "Ukupna porucena kolicina: %s kilograma" (:general_amount (nth (fodb/general-food-statistic) 0)))]
    [:hr]
    [:h4 "Statistika narudzbina po mesecu:"]
    (all-statistic-for-months (fodb/orders-per-month))))


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


(defn form-new-order [& [msg]]
  (html5
    (when msg [:div.alert.alert-danger msg])
    [:body
     (form/form-to [:post (str "/orders/new/" (odb/get-next-id))]

                   [:div.form-group
                    (form/label "full_name" "Narucilac: ")
                    (form/text-field {:class "form-control"} "full_name")]
                   [:hr]
                   [:div.form-group
                    (form/label "do_date" "Datum isporuke (dd.mm.gggg.) : ")
                    (form/text-field {:class "form-control"} "do_date" "1.1.2023")]
                   [:hr]
                   [:div.form-group
                    (form/label "city_part" "Deo grada: ")
                    ;(form/text-field "location" "Location")
                    [:div.div-separator (form/drop-down {:class "form-class"} "city_part" ["Kolonija" "Centar" "HRS" "HIM", "VUK", "Naselje", "Mikulja", "Bolnica", "Gimnazija", "Opeka", "Jezero" "Suleiceva" "Zelengora" "GOSA" "Kiseljak"])]]
                   [:hr]
                   [:div.form-group
                    (form/label "street" "Ulica i broj: ")
                    (form/text-field {:class "form-control"} "street")]
                   [:hr]
                   [:div.form-group
                    (form/label "delivered" "Isporuceno (DA/NE) : ")
                    ;(form/text-field "delivered" "delivered")
                    [:div.div-separator (form/drop-down {:class "form-class"} "delivered" [ "NE"])]]
                   [:hr]
                   [:div.form-group
                    (form/label "package_name" "Kolicina (br.komada) : ")
                    [:div.div-separator (form/drop-down {:class "form-class"} "package_name" ["10komada" "20komada" "30komada" "40komada" "60komada" "70komada" "100komada" "120komada" "150komada" "180komada" "210komada" "240komada" "270komada" "300komada"])]]
                    [:hr]
                   [:div.form-group
                    (form/label "phone" "Telefon: ")
                    (form/text-field {:class "form-control"} "phone")]

                   (form/hidden-field "id" (odb/get-next-id))
                   (anti-forgery-field)

                   (form/submit-button {:class "btn btn-primary"} "Sacuvaj narudzbinu"))]))

(defn new-order [order]
  (odb/new-order order))

;(new-order {:full_name "GIARDINO" :do_date "23.12.2022" :city_part "Centar" :street "Glavna uliva" :delivered "NE" :package_name "10 komada"})



(defn edit-order [order]
  (html5
    [:body
     ;[:p "Broj porudzbine koja se menja: " (:id order)]
     (form/form-to [:post (if order
                            (str "/orders/" (:id order))
                            "/orders")]
                   [:div.form-group
                    (form/label "full_name" "Narucilac: ")
                    (form/text-field {:class "form-control"} "full_name" (:full_name order))]
                   [:hr]
                   [:div.form-group
                    (form/label "do_date" "Datum isporuke (dd.mm.gggg.) : ")
                    (form/text-field {:class "form-control"} "do_date" (:do_date order))]
                   [:hr]
                   [:div.form-group (form/label "city_part" "Deo grada: ")
                    ;(form/text-field "location" (:location order))
                    (form/drop-down {:class "form-class"} "city_part" ["Kolonija" "Centar" "HRS" "HIM", "VUK", "Naselje", "Mikulja", "Bolnica", "Gimnazija", "Opeka", "Jezero" "Suleiceva" "Zelengora" "GOSA" "Kiseljak"])]
                   [:hr]
                   [:div.form-group
                    (form/label "street" "Ulica i broj: ")
                    (form/text-field {:class "form-control"} "street" (:street order))]
                   [:hr]
                   [:div.form-group
                    (form/label "delivered" "Isporuceno (DA/NE): ")
                    ;(form/text-field "deliverede" (:delivered order))
                    (form/drop-down {:class "form-class"} "delivered" ["DA" "NE"])]
                   [:hr]
                   [:div.form-group
                    (form/label "package_name" "Kolicina (br.komada) : ")
                    [:div.div-separator (form/drop-down {:class "form-class"} "package_name" ["10komada" "20komada" "30komada" "40komada" "60komada" "70komada" "100komada" "120komada" "150komada" "180komada" "210komada" "240komada" "270komada" "300komada"])]]

                   (form/hidden-field "id" (:id order))
                   (anti-forgery-field)

                   (form/submit-button {:class "btn btn-primary"} "Sacuvaj izmene"))]))

(defn form-delete-order [order]
  (html5
    [:body
     [:p "Broj porudzbine koja se brise: " (:id order)]
     (form/form-to [:post (if order
                            (str "/orders/delete/" (:id order))
                            "/orders")]

                   [:div.form-group
                    (form/label "full_name" "Narucilac: ")
                    (form/text-field {:class "form-class"} "full_name" (:full_name order))]
                   [:hr]
                   [:div.form-group
                    (form/label "do_date" "Datum isporuke (dd.mm.gggg.) : ")
                    (form/text-field {:class "form-class"} "do_date" (:do_date order))]
                   [:hr]
                   [:div.form-group
                    (form/label "city_part" "Deo grada: ")
                    (form/text-field {:class "form-class"} "city_part" (:city_part order))]
                   [:hr]
                   [:div.form-group
                    (form/label "street" "Ulica i broj: ")
                    (form/text-field {:class "form-class"} "street" (:street order))]
                   [:hr]
                   [:div.form-group
                    (form/label "delivered" "Isporuceno (DA/NE): ")
                    (form/text-field {:class "form-class"} "deliverede" (:delivered order))]
                   [:hr]
                   [:div.form-group
                    (form/label "package_name" "Kolicina (br.komada) : ")
                    (form/text-field {:class "form-class"} "package_name" (:amount order))]

                   (form/hidden-field "id" (:id order))
                   (anti-forgery-field)

                   (form/submit-button {:class "btn btn-primary"} "Izbrisi"))]))



(defn form-delete-food-order [forder]
  (html5
    [:body
     [:p (:id forder)]
     (form/form-to [:post (if forder
                            (str "/food-order/delete/" (:id forder))
                            "/all-food-orders/delete")]

                   [:div.form-group
                    (form/label "do_date" "Datum isporuke (dd.mm.gggg.) : ")
                    (form/text-field {:class "form-class"} "do_date" (:do_date forder))]
                   [:hr]
                   [:div.form-group
                    (form/label "month_name" "Mesec: ")
                    (form/text-field {:class "form-class"} "month_name" (:month_name forder))]
                   [:hr]
                   (form/hidden-field "type_id" (:type_id forder))
                   (form/hidden-field "id" (:id forder))
                   (anti-forgery-field)

                   (form/submit-button {:class "btn btn-primary"} "Izbrisi"))]))


;(edit-order (db/get-order-by-id 2))
;(fodb/get-next-food-id)

(defn form-new-food []
  (html5
    [:body
     (form/form-to [:post (str "/food-order/new/" (fodb/get-next-food-id))]

                   [:div.form-group
                    (form/label "do_date" "Datum isporuke (dd.mm.gggg.) : ")
                    (form/text-field {:class "form-class"} "do_date" "dd.mm.gggg.")]
                   [:hr]
                   [:div.form-group
                    (form/label "month_name" "Mesec: ")
                    ;(form/text-field "location" "Location")
                    [:div.div-separator (form/drop-down {:class "form-class"} "month_name" ["Januar" "Februar" "Mart" "April", "Maj", "Jun", "Jul", "Avgust", "Septembar", "Oktobar", "Novembar" "Decembar"])]]
                   [:hr]
                   [:div.form-group
                    (form/label "type_name" "Vrsta hrane: ")
                    ;(form/text-field "delivered" "delivered")
                    [:div.div-separator (form/drop-down {:class "form-class"} "type_name" ["Pantelic zito" "Pantelic vitamini"])]]
                   [:hr]
                   [:div.form-group
                    (form/label "amount" " Porucena kolicina - 1000kg (oznacite polje!)")
                    [:div.div-separator (form/check-box {:class "form-class"} "amount" true 1000)]]
                   (form/hidden-field "id" (fodb/get-next-food-id))
                   (anti-forgery-field)

                   (form/submit-button "Save order"))]))



;ova forma ce biti koriscena za kreiranje i editovanje narudzbine
;ali po istom principu moze da se koristi za create/update bilo cega
;ovo cu verovatno ovako da pravim za pitanja


;(defn form-search-order []
;  (html5
;    [:body
;     (form/form-to [:post (str "/orders/search/")]
;
;                   [:div.form-group
;                    (form/label "full_name" "Pretraga po nazivu narucioca: ")
;                    (form/text-field {:class "form-control"} "full_name")]
;                   [:hr]
;                   (anti-forgery-field)
;
;                   (form/submit-button {:class "btn btn-primary"} "Pretrazi"))]))

(defn all-orderers [orderers]
  (base [:h1 "Svi porucioci"]
        (for [o orderers]
          [:div
           [:h4 [:a {:href (str "/orderer/" (:full_name o))} " Porucilac: " (:full_name o)]]])))
(all-orderers (odb/list-orderers-names))

(defn all-food-types [food-types]
  (base [:h1 "Vrste hrane"]
        (for [ft food-types]
          [:div
           [:h4 [:a {:href (str "/food-type/"(:type_name ft))} " Vrsta hrane: " (:type_name ft)]]])))
(all-food-types (fodb/list-type-names))

(defn food-order-view2 [{id :id amount :amount do_date :do_date month_name :month_name}]
  (html5
    [:li (format " ID naruzbine: %s         Kolicina: %s           Datum isporuke: %s      " id amount do_date )]))

(defn food-orders-view2 [forders]
  (html5 [:ul
          (map food-order-view2 forders)]))


;;REGISTRACIJA/LOGOVANJE USER-a

(defn user-register [& [msg]]
  (html5
    (when msg [:div.alert.alert-danger msg])
    [:body
     (form/form-to
       [:post (str "/user/register/" (udb/get-next-user-id))]

       [:div.form-group
        (form/label "owner_name" "Ime vlasnika:  ")
        (form/text-field {:class "form-control"} "owner_name")]
       [:hr]
       [:div.form-group
        (form/label "owner_surname" "Prezime vlasnika:  ")
        (form/text-field {:class "form-control"} "owner_surname")]
       [:hr]
       [:div.form-group
        (form/label "phone" "Broj telefona (06*-****-***) :  ")
        (form/text-field {:class "form-control"} "phone")]
       [:hr]
       [:div.form-group
        (form/label "password" "Lozinka:  ")
        (form/password-field {:class "form-control"} "password")]
       (anti-forgery-field)
       [:hr]
       (form/submit-button {:class "btn btn-primary"} "Registruj se")
       )]))

(defn user-login [& [msg]]
  (html5
    (when msg [:div.alert.alert-danger msg])
    [:body
     (form/form-to
       [:post (str "/user/login")]

       [:div.form-group
        (form/label "phone" "Broj telefona (06*-****-***) :  ")
        (form/text-field {:class "form-control"} "phone")]
       [:hr]
       [:div.form-group
        (form/label "password" "Lozinka:  ")
        (form/password-field {:class "form-control"} "password")]
       (anti-forgery-field)
       [:hr]
       (form/submit-button {:class "btn btn-primary"} "Uloguj se")
       )]))