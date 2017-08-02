# moarweba

This seed repo provides a collection of Clojure libraries patterned together to get you up and running in less than one minute with a solid Clojure REST-ful Web Server.

It is designed to offer you a great starting point for your project. 

## Usage

#### Prerequisites:

[leiningen](https://leiningen.org/) and a modern JDK installed.

### Starting Up

```
lein new moarweba myproject
cd myproject
lein repl
```


**Congratulations!**  You have a running clojure web server.


in _development:_

browse to [localhost:8080](http://localhost:8080) in your browser, or 

`> curl -X GET http://localhost:8080` if that's your thing.


in _production:_

```
cd myproject
lein uberjar
java -jar target/myproject-< VERSION >-standalone.jar start 
```

###  Motivation

I write a bunch of web applications. Plus, I like Clojure. A lot.

What has emerged is this loose collection of libraries and patterns that I've put together to quickly get up and running to get stuff done.  

This is primarily the basic structure I was repeatedly copying over from project to project.

### What's in the Box

A fully RESTful Web Server, of course!

It's got: 
* an HTTP(s) server
* Routing
* authentication and authorization.  
* the stub for a backend (database) connection
* some other utility stuff to make life easier


Moarweba rigs up the following great Clojure libraries for you:

| included Library  | Reason  | 
|---|---|
|[http-kit](http://www.http-kit.org/) | a high-performance http server with WebSocket support.
|[Ring](https://github.com/ring-clojure/ring) | Beautiful HTTP-as-API abstraction. Used extensively throughout the Clojure community.|
|[liberator](https://clojure-liberator.github.io/liberator/) |  Define Resources. Handle Requests exactly how you want to. |
|[bidi](https://github.com/juxt/bidi) |  Routing as Data Structures. |
|[buddy-auth](https://github.com/funcool/buddy-auth), [buddy-hashers](https://github.com/funcool/buddy-hashers), and [buddy-sign](https://github.com/funcool/buddy-sign)| because Security isn't an afterthought. |
|[cheshire](https://github.com/dakrone/cheshire)| because JSON |

You can keep none, some, or all of these libraries as you progress in building up your project. Your application will dictate what to keep and what to discard, but these included web libraries are _generally_ useful regardless of the domain.

### What's Going On Inside?

ok, peek into `src/moarweba/`  and you'll see the following files:

##### server.clj

This is the actual http server.  It has some stuff to handle http requests, notably some Ring-style middleware, which has become a de-facto standard for handling web requests.

You shouldn't need to mess with this file too much.

At the very bottom though, the last line, you'll find

```
...
(reload)  ;; Comment out this line BEFORE pushing to production.
```

You can `%Eval` the file `server.clj` in development, and this will reload the server.

Remember to comment out `(reload)` before pushing to production so the `-main` function will load properly and be picked up by `java -jar <myapp>.jar start &`

##### handlers.clj

this is really an implementation of liberator for defining your REST resources combined with bidi for routing.  Define all of your URL endpoints in here, and use the various `defresources` to handle them appropriately.

##### bankend.clj

I'm not imposing any opinions about what backend you should use. Some of you like databases. Some of you don't.  If you do, here's a good starting place to put your datastore stuff.

##### util.clj

Utility stuff.  Yep. 

### Wait... Is this just another 'Framework'  ?

No.

## No License

Distributed without license restrictions to a free and open world with the hope that it is useful.

Copyright © 2017 tuddman
