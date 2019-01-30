(ns {{ns-name}}.handlers
  (:require
   [bidi.ring :as bidiring :refer (make-handler)]
   [buddy.auth :refer [authenticated? throw-unauthorized]]
   [buddy.auth.backends.token :refer [jws-backend]]
   [buddy.sign.jwt :as jwt]
   [clj-time.core :as t]
   [liberator.core :refer [defresource]]
   [{{ns-name}}.db :as db]))


(def secret "replace-me-with-an-environment-variable")

; Create an instance of auth backend.
(def auth-backend  (jws-backend
                    {:secret secret
                     :options {:alg :hs256}}))

(defn make-token
  [claims dur]
  (let [c {:exp (t/plus (t/now) (t/days dur))}]
    (jwt/sign (merge claims c) secret {:alg :hs256})))

(defn- param [context param] (-> context :request :route-params param))

(defn- get-body [context] (-> context :request :body))

(defresource employees
  :available-media-types ["application/json"]
  :allowed-methods [:get :post]
  :handle-ok (fn [context]  (db/all-employees))
  :post! (fn [context]
           (let [record  (db/new-employee (get-body context))]
             {::response record}))
  :handle-created ::response)

(defresource employee
  :available-media-types ["application/json"]
  :allowed-methods [:get :put :delete]
  :handle-ok (fn [context] (db/get-employee (param context :id)))
  :put! (fn [context] {:updated (db/update-employee (param context :id) (dissoc (get-body context) :Status))})
  :delete! (fn [context] (db/delete-employee (param context :id))))

(defresource deactivation
  :available-media-types ["application/json"]
  :allowed-methods [:delete]
  :authorized? (fn [context] (authenticated? (:request context)))
  :delete! (fn [context] (db/set-to-inactive (param context :id))))

(defresource token
  :available-media-types ["application/json"]
  :allowed-methods [:get]
  :handle-ok (fn [context] {:token (str "Token " (make-token {} 180))}))

(defresource home
  :available-media-types ["application/json"]
  :allowed-methods [:get]
  :handle-ok (fn [context] {:status "running"}))

;; ---------------------------------  ROUTES


(def handler
  (make-handler
   ["/" [[""                      home]
         ["employees"        employees]
         [["employees/" :id]  employee]
         [["employees/deactivate/" :id]  deactivation]
         ["token"  token]
[true (fn [_] {:status 404})]]]))
