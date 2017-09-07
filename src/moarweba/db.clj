(ns moarweba.db
  (:require [clojure.spec.alpha :as s]))

(s/def ::foo string?)
(s/def ::bar string?)
(s/def ::baz string?)

(s/def ::fields
  (s/keys :req-un [::foo ::bar]
          :opt-un [::baz]))

(defn call-backend
  []
  {:foo "bar"})

