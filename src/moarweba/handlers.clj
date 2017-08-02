(ns moarweba.handlers
  (:require
   [bidi.bidi :as bidi :refer [match-route path-for]]
   [bidi.ring :as bidiring :refer (make-handler)]
   [buddy.auth :refer [authenticated? throw-unauthorized]]
   [buddy.auth.backends.token :refer [jws-backend]]
   [clj-time.core :as t]
   [liberator.core :refer [defresource]]
   [moarweba.db :as db]
   [moarweba.util :as util]))

; Create an instance of auth backend.
(def auth-backend  (jws-backend
                    {:secret util/secret
                     :options {:alg :hs512}}))

(defn- allowed?
  [context profile]
  (and
   (authenticated? (:request context))
   (some #(= profile %)
         (flatten [(get-in context [:request :identity :profile])]))))

(defn- param
  [context param]
  (-> context :request :route-params param))

(defn- get-body
  [context]
  (-> context :request :body))
 
(defresource backend
  :available-media-types ["application/json"]
  :allowed-methods [:post]
  :post! (fn [context]
           (let [data-to-fetch  (db/call-backend (get-body context))]
             {::response data-to-fetch}))
  :handle-created ::response)
 
(defresource echo
  :available-media-types ["application/json"]
  :allowed-methods [:get :post!]
  :handle-ok (fn [context]  "ping")
  :post! (fn [context] {::response (get-body context)})
  :handle-created ::response)

(defresource home
  :available-media-types ["application/json"]
  :allowed-methods [:get]
  :handle-ok (fn [context] {:Hello "World"}))

(defresource status
  :available-media-types ["application/json"]
  :allowed-methods [:get]
  :handle-ok (fn [context] {:status "ok" :version (System/getProperty "moarweba.version")}))
 
;; ---------------------------------  ROUTES


(def handler
  (make-handler
   ["/" [[""                     home]
         ["backend"           backend]
         ["echo"                 echo]
         ["status"             status]
         [true (fn [_] {:status 404})]]]))
