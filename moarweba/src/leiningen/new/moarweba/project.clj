(defproject {{ns-name}} "0.1.0-SNAPSHOT"
  :description ""
  :url "https://github.com/tuddman/{{ns-name}}"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha15"]
                 [org.clojure/tools.cli "0.3.5"]
                 [bidi "2.0.16"]
                 [bouncer "1.0.1"]
                 [buddy/buddy-auth "1.4.1"]
                 [buddy/buddy-core "1.2.0"]
                 [buddy/buddy-hashers "1.2.0"]
                 [buddy/buddy-sign "1.5.0"]
                 [cheshire "5.7.0"]
                 [clj-http "3.4.1"]
                 [com.taoensso/timbre "4.8.0"]
                 [http-kit "2.2.0"]
                 [liberator "0.14.1"]
                 [compojure "1.5.2"] 
                 [ring/ring-core "1.5.1"]
                 [ring/ring-jetty-adapter "1.5.1"]
                 [ring/ring-json "0.4.0"]
                 [ring-cors "0.1.9"]
                 [ring/ring-defaults "0.2.3"]
                 [ring-middleware-format "0.7.2" :exclusions [ring]]]
  :min-lein-version "2.5.1"
  :main {{ns-name}}.server
  :profiles  {:dev  {:dependencies  [[org.clojure/test.check "0.9.0"]]}
              :uberjar  {:main {{ns-name}}.server, :aot :all}
              :production  {:env {:production true}}}
  :uberjar-name "{{ns-name}}-0.1.0-SNAPSHOT-standalone.jar"  
  :repl-options {:timeout 60000}) 
