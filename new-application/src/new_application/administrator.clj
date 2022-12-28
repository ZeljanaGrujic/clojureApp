(ns new-application.administrator)
;napisacu check login password proveru

(def administrator-login (or (System/getenv "ADMIN_LOGIN") "admin"))
(def administrator-pass (or (System/getenv "ADMIN_PASSWORD") "admin"))

;check user intput
(defn check-credentials [{ login :login password :password}]
  (and (= login administrator-login)
       (= password administrator-pass)))
(check-credentials {:login "admin" :password "admin" })

;wrepovacemo nas app hendler with session midleware