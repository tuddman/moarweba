(ns {{ns-name}}.util)

(defn env-var
  "fetches a given ENV"
  [^String s]
  (get (System/getenv) s))