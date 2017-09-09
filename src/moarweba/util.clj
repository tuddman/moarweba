(ns moarweba.util)

(def secret "replace-me-with-an-environment-variable")

(defn env-var
  "fetches a given ENV"
  [^String s]
  (get (System/getenv) s))
