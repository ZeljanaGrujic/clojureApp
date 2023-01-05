(ns new-application.administrator)
;napisacu check login password proveru

;(def administrator-login (or (System/getenv "ADMIN_LOGIN") "admin"))
;(def administrator-pass (or (System/getenv "ADMIN_PASSWORD") "admin"))


(def ^:dynamic administrator-login "admin")
(def ^:dynamic administrator-pass "admin")
(binding [administrator-login "admin"
          administrator-pass "admin"])

;check user intput
(defn check-credentials [{ login :login password :password}]
  (and (= login administrator-login)
       (= password administrator-pass)))
(check-credentials {:login "admin" :password "admin" })

;wrepovacemo nas app hendler with session midleware

;clojure for the brave and truth, thread local binding
; (bind user pera... i onda ce u okviru tog lokalnog bindinga user biti pera, taj bajdning je vezan za taj scope
;menaging clojure session with ring, potraziti na netu
;Eric Normand pogledati ima resursa, pogledati ring cookie ring midleware i td...