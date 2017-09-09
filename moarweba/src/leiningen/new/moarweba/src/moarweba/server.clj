(ns {{ns-name}}.server
  (:import (java.net InetAddress))
  (:require [org.httpkit.server       :refer [run-server]]
            [buddy.auth.middleware    :refer [wrap-authentication wrap-authorization]]
            [clojure.string           :as    cstr]
            [clojure.tools.cli        :refer [parse-opts]]
            [liberator.dev            :refer [wrap-trace]]
            [ring.middleware.cors     :refer [wrap-cors]]
            [ring.middleware.defaults :refer :all]
            [ring.middleware.json     :refer [wrap-json-body]]
            [{{ns-name}}.handlers        :refer [handler auth-backend]]
            [{{ns-name}}.util            :as    util]
            [taoensso.timbre :as log])
  (:gen-class))

(defn- wrap-log
  [handler log-f]
  (fn [request]
    (log/debug (log-f request))
    (handler request)))

;; ---------------------------------  CORS

(defn- cors-ring-handler
  "Wraps the ring handler to support CORS"
  [handler]
  (fn [request]
;    (log/info request)    ; uncomment this line if you want to inspect each request as it comes in.
    (let [resp (handler request)]
      ; If the response is nil, don't pass through ring-cors
      (if ((complement nil?) resp)
        (let [cors-wrapped-handler
              (wrap-cors
               (fn [request] resp)
               :access-control-allow-origin #".*"
               :access-control-allow-headers #{:accept :content-type :authorization :origin}
               :access-control-allow-methods #{:get :patch :put :post :delete})]
          (cors-wrapped-handler request))))))

;; ---------------------------------  Application

(def app (-> (cors-ring-handler handler)
             (wrap-authentication auth-backend)
             (wrap-authorization auth-backend)
    		 #_(wrap-anti-forgery) 
             #_(wrap-log)
             (wrap-defaults (assoc-in  site-defaults [:security :anti-forgery] false))
             (wrap-json-body {:keywords? true})
             #_(wrap-trace :header :ui)))

;; ---------------------------------  Server

(defonce server (atom nil))

(defn stop-server []
  (when-not (nil? @server)
    ;; graceful shutdown: wait 100ms for existing requests to be finished
    ;; :timeout is optional, when no timeout, stop immediately
    (@server :timeout 100)
    (reset! server nil)))

;; for use while in development. 
;; comment out last line:  (reload)  in production.
(defn reload []
  (do
    (stop-server)
    (reset! server (run-server #'app {:port (Integer. (util/env-var "PORT"))}))))

(def cli-options
  [;; First three strings describe a short-option, long-option with optional
   ;; example argument description, and a description. All three are optional
   ;; and positional.
   ["-p" "--port PORT" "Port number"
    :default 80
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ["-H" "--hostname HOST" "Remote host"
    :default (InetAddress/getByName "localhost")
    ;; Specify a string to output in the default column in the options summary
    ;; if the default value's string representation is very ugly
    :default-desc "localhost"
    :parse-fn #(InetAddress/getByName %)]
   ;; If no required argument description is given, the option is assumed to
   ;; be a boolean option defaulting to nil
   [nil "--detach" "Detach from controlling process"]
   ["-v" nil "Verbosity level; may be specified multiple times to increase value"
    ;; If no long-option is specified, an option :id must be given
    :id :verbosity
    :default 0
    ;; Use assoc-fn to create non-idempotent options
    :assoc-fn (fn [m k _] (update-in m [k] inc))]
   ["-h" "--help"]])

(defn usage [options-summary]
  (->> ["{{ns-name}}"
        ""
        "Usage: ..standalone.jar [options] action"
        ""
        "Options:"
        options-summary
        ""
        "Actions:"
        "  start    Start a new server"
        "  stop     Stop an existing server"
        ""
        "Please refer to the manual page for more information."]
       (cstr/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (cstr/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    ;; Handle help and error conditions
    (cond
      (:help options) (exit 0 (usage summary))
      (not= (count arguments) 1) (exit 1 (usage summary))
      errors (exit 1 (error-msg errors)))
    ;; Execute program with options
    (case (first arguments)
      "start"
      (->
       (reset! server (run-server #'app {:port (Integer. (util/env-var "PORT"))}))
       (log/info (str "server started. listen on 0.0.0.0:" (util/env-var "PORT"))))
      "stop" (stop-server)
      (exit 1 (usage summary)))))

(reload)  ;; Comment this line BEFORE pushing to production.
