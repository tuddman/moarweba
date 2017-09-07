(defproject {{ns-name}} "0.1.0"
  :description ""
  :url "https://github.com/tuddman/{{ns-name}}"
  :license {:name "unlicense"
            :url "https://unlicense.org/"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha16"]
                 [org.clojure/tools.cli "0.3.5"]
                 [bidi "2.1.2"]
                 [bouncer "1.0.1"]
                 [buddy/buddy-auth "2.1.0"]
                 [buddy/buddy-core "1.4.0"]
                 [buddy/buddy-hashers "1.3.0"]
                 [buddy/buddy-sign "2.2.0"]
                 [cheshire "5.8.0"]
                 [clj-http "3.7.0"]
                 [com.taoensso/timbre "4.10.0"]
                 [http-kit "2.2.0"]
                 [liberator "0.15.1"]
                 [compojure "1.6.0"] 
                 [ring/ring-core "1.6.2"]
                 [ring/ring-jetty-adapter "1.6.2"]
                 [ring/ring-json "0.4.0"]
                 [ring-cors "0.1.11"]
                 [ring/ring-defaults "0.3.1"]
                 [ring-middleware-format "0.7.2" :exclusions [ring]]]
  :min-lein-version "2.5.1"
  :main {{ns-name}}.server
  :profiles  {:dev  {:dependencies  [[org.clojure/test.check "0.9.0"]]}
              :uberjar  {:main {{ns-name}}.server, :aot :all}
              :production  {:env {:production true}}}
  :uberjar-name "{{ns-name}}-0.1.0-standalone.jar"  
  :repl-options {:timeout 60000}) 
