(ns {{ns-name}}.db
  (:require [clojure.java.jdbc :refer :all]
            [java-jdbc.sql :as sql]
            [clojure.spec.alpha :as s]
            [clj-uuid :as cljuuid]
            [clj-time.core :as t]))

;; --------------------------------- Employee Specs

(def date-regex #"(\d{4})-(\d{2})-(\d{2})")

(s/def ::id uuid?)
(s/def ::FirstName string?)
(s/def ::MiddleInitial char?)
(s/def ::LastName string?)
(s/def ::DateOfBirth (s/and string? #(re-matches date-regex %)))
(s/def ::DateOfEmployment (s/and string? #(re-matches date-regex %)))
(s/def ::Status #{"ACTIVE" "INACTIVE"})

(s/def ::fields
  (s/keys :req-un [::id
                   ::FirstName
                   ::LastName
                   ::DateOfBirth
                   ::DateOfEmployment
                   ::Status]
          :opt-un [::MiddleInitial]))

;; --------------------------------- DB Setup

(def db
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     "db/database.db"})

(defn create-db []
  (try
    (db-do-commands db
                    (create-table-ddl :employees
                                      [[:id :text]
                                       [:FirstName :text]
                                       [:MiddleInitial :text]
                                       [:LastName :text]
                                       [:DateOfBirth :text]
                                       [:DateOfEmployment :text]
                                       [:Status :text]]))
    (catch Exception e (println e))))

(create-db)

;; --------------------------------- DB Functions

(defn all-employees []
  (query db (sql/select * :employees (sql/where {:Status "ACTIVE"}))))

(defn new-employee
  [record]
  (insert! db :employees (assoc record :Status "ACTIVE" :id (cljuuid/v1))))

(defn get-employee
  [id]
  (first (query db (sql/select * :employees (sql/where {:Status "ACTIVE" :id id})))))

(defn update-employee
  [id & [props]]
  (update! db :employees props ["id = ?" id]))

(defn delete-employee
  [id]
  (delete! db :employees ["id = ?" id]))

(defn set-to-inactive
  [id]
(update! db :employees {:Status "INACTIVE"} ["id = ?" id]))
