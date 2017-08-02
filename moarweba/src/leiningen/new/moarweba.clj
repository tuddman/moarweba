(ns leiningen.new.moarweba
  (:use [leiningen.new.templates :only [renderer name-to-path sanitize-ns ->files]]))

(def render (renderer "moarweba"))

(defn moarweba
  [name]
  (let [data {:name name
              :ns-name (sanitize-ns name)
              :sanitized (name-to-path name)}]
    (->files data ["project.clj" (render "project.clj" data)]
["test/{{sanitized}}/core_test.clj" (render "test/moarweba/core_test.clj" data)]
["src/{{sanitized}}/core.clj" (render "src/moarweba/core.clj" data)]
["src/{{sanitized}}/server.clj" (render "src/moarweba/server.clj" data)]
)))