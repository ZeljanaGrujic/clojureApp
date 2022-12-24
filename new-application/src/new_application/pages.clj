(ns new-application.pages
  (:require
    [hiccup.page :refer [html5]]
    [hiccup.form :as form]
    [ring.util.anti-forgery :refer (anti-forgery-field)]
    [new-application.db :as db]))


(defn base [& body]
  (html5
    [:head [:title "GRUJIC- agro"]]
    [:body
     [:h1 "Dnevnik klijenata i prodaje jaja"]
     [:a {:href "/"} "Pocetna stranica"]
     [:hr]
     body]))

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
           [:h4 [:a {:href (str "/orders/" (:id o))}"ID porudzbine " (:id o) "      Porucilac:" (:full_name o) "            Datum isporuke:" (:do_date o)]])))

(defn index-for-update [orders]
  (base [:h1 "Choose order to update"]
        (for [o orders]
          [:h4 [:a {:href (str "/orders/new/edit/" (:id o))}"ID porudzbine " (:id o) "      Porucilac:" (:full_name o) "            Datum isporuke:" (:do_date o)]])))

(defn index-for-delete [orders]
  (base [:h1 "Choose order to delete"]
        (for [o orders]
          [:h4 [:a {:href (str "/orders/new/delete/" (:id o))}"ID porudzbine " (:id o) "      Porucilac:" (:full_name o) "            Datum isporuke:" (:do_date o)]])))

(defn view-order [order]
  (base
    [:a {:href (str "/orders/new/edit/" (:id order))} "Edit order directly"]
    [:hr]
    [:small (:id order)]
    [:h1 (:full_name order) "     " (:amount order) "     " (:do_date order)]
    [:h2 (:city_part order) "     " (:street order) "     " (:delivered order)]))

(view-order {:id 1,
             :full_name "Srecko Grujic",
             :amount "0",
             :do_date "00.00.0000",
             :city_part "Naselje",
             :street "Dr. Cambe 10",
             :delivered "DA"})

(index (db/list-orders))
(view-order (db/get-order-by-id 1))


;(defn order-view [{id :id full_name :full_name amount :amount do_date :do_date city_part :city_part street :street delivered :delivered}]
;  (html5
;    [:li (format " Order id: %s      Full_name: %s        Amount: %s        Do_date: %s     City part: %s   Street: %s    Delivered: %s" id full_name amount do_date city_part street delivered)]))
;
;(defn orders-view [orders]
;  (html5 [:ul
;          (map  order-view orders)]))

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
                   (form/label "amount" "Amount: ")
                   (form/text-field "amount" "amount")
                   (form/label "do_date" "Do date: ")
                   (form/text-field "do_date" "do date")
                   (form/label "city_part" "City part: ")
                   ;(form/text-field "location" "Location")
                   [:div.div-separator (form/drop-down {:class "form-class"} "city_part" ["Kolonija" "Centar" "HRS" "HIM", "VUK", "Naselje", "Mikulja", "Bolnica", "Gimnazija", "Opeka", "Jezero"])]
                   (form/label "street" "Street: ")
                   (form/text-field "street" "street")
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
                   (form/label "amount" "Amount: ")
                   (form/text-field "amount" (:amount order))
                   (form/label "do_date" "Do date: ")
                   (form/text-field "do_date" (:do_date order))
                   (form/label "city_part" "City part: ")
                   ;(form/text-field "location" (:location order))
                   (form/drop-down {:class "form-class"} "city_part" ["Kolonija" "Centar" "HRS" "HIM", "VUK", "Naselje", "Mikulja", "Bolnica", "Gimnazija", "Opeka", "Jezero"])
                   (form/label "street" "Street: ")
                   (form/text-field "street" (:street order))
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
                   (form/label "amount" "Amount: ")
                   (form/text-field "amount" (:amount order))
                   (form/label "do_date" "Do date: ")
                   (form/text-field "do_date" (:do_date order))
                   (form/label "city_part" "City part: ")
                   (form/text-field "city_part" (:city_part order))
                   (form/label "street" "Street: ")
                   (form/text-field "street" (:street order))
                   (form/label "delivered" "Delivered (DA/NE): ")
                   (form/text-field "deliverede" (:delivered order))
                   (form/hidden-field "id" (:id order))
                   (anti-forgery-field)

                   (form/submit-button "Delete order"))]))


;(edit-order (db/get-order-by-id 2))



;ova forma ce biti koriscena za kreiranje i editovanje narudzbine
;ali po istom principu moze da se koristi za create/update bilo cega
;ovo cu verovatno ovako da pravim za pitanja


