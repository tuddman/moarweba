(defproject {{ns-name}} "0.1.0"
  :description ""
  :url "https://github.com/tuddman/{{ns-name}}"
  :license {:name "unlicense"
            :url "https://unlicense.org/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/java.jdbc "0.7.8"]
                 [org.clojure/tools.cli "0.4.1"]
                 [org.xerial/sqlite-jdbc "3.25.2"]
                 [bidi "2.1.5"]
                 [bouncer "1.0.1"]
                 [buddy/buddy-auth "2.1.0"]
                 [buddy/buddy-core "1.5.0"]
                 [buddy/buddy-hashers "1.3.0"]
                 [buddy/buddy-sign "2.2.0"]
                 [cheshire "5.8.1"]
                 [clj-http "3.9.1"]
                 [com.taoensso/timbre "4.10.0"]
                 [http-kit "2.3.0"]
                 [liberator "0.15.2"]
                 [compojure "1.6.1"] 
                 [ring/ring-core "1.7.1"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [ring/ring-json "0.4.0"]
                 [ring-cors "0.1.13"]
                 [ring/ring-defaults "0.3.2"]
                 [ring-middleware-format "0.7.3" :exclusions [ring]]]
  :min-lein-version "2.5.1"
  :main {{ns-name}}.server
  :profiles  {:dev  {:dependencies  [[org.clojure/test.check "0.9.0"]]}
              :uberjar  {:main {{ns-name}}.server, :aot :all}
              :production  {:env {:production true}}}
  :uberjar-name "{{ns-name}}-0.1.0-standalone.jar"  
  :repl-options {:timeout 60000}
  :template-additions [".gitignore" "README.md"]) 
