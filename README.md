# ACCOUNT MICROSERVICES DEMO

## Build status

[![Build Status](https://travis-ci.org/chinnonsantos/account-service.svg?branch=master)](https://travis-ci.org/chinnonsantos/account-service)

This project deals with an account microservice created from the leiningen '[compojure][]' template.

## Prerequisites

You will need [Leiningen][] 2.9.1 or above installed.

**Libraries:**

- [midje][] 1.9.9 (:dev)
- [ring/ring-core][] 1.7.1(:dev)
- [ring/ring-jetty-adapter][] 1.7.1 (:dev)
- [clj-http][] 3.10.0
- [cheshire][] 5.9.0
- [ring-json][] 0.5.0

**Plugins:**

- [lein-midje][] 3.2.1 (:dev)
- [lein-cloverage][] 1.1.2 (:dev)

[compojure]: https://github.com/weavejester/compojure
[leiningen]: https://github.com/technomancy/leiningen
[midje]: https://clojars.org/midje
[ring/ring-core]: https://clojars.org/ring/ring-core
[ring/ring-jetty-adapter]: https://clojars.org/ring/ring-jetty-adapter
[clj-http]: https://clojars.org/clj-http
[cheshire]: https://clojars.org/cheshire
[ring-json]: https://clojars.org/ring/ring-json
[lein-midje]: https://clojars.org/lein-midje
[lein-cloverage]: https://clojars.org/lein-cloverage

## Running

To start a web server for the application, run:

    lein ring server-headless

> Default ring port changed from ~~3000~~ to **9001**

## Running tests (TDD)

To test the project, run:

    lein midje

To test the project in development stage, run:

    lein midje :autotest

> Automatically reloading the test with each change

To test only **Unit tests** of the project, run:

    lein midje :filter unit

To test only **Assertion tests** of the project, run:

    lein midje :filter assertion

To check test coverage, run:

    lein cloverage --runner :midje

## E2E Testing

To perform a quick **End-to-End** test on this service (with cURL)

- Check health

      curl http://localhost:9001/

- Get accounts list

      curl http://localhost:9001/account/

- Get account info (account details)

      curl http://localhost:9001/account/:account-id/

  > The _:account-id_ is an [UUID][] automatically generated by the service after registration

- Get account info by customer ID (account details)

      curl http://localhost:9001/account/from-customer/:customer-id/

  > The _:customer-id_ is an [UUID][] informed when creating new account, see the example _'Create an account'_

- Create an account

      curl -X POST \
      -d '{"customer-id": "7724236d-8544-4657-8863-75269c262b62", "bank-branch": "0001", "bank-account": "1234567-8", "limit": 15000}' \
      -H "Content-Type: application/json" \
      localhost:9001/account/

- Hitting an invalid route

      curl http://localhost:9001/invalid/

To perform a complete automated **End-to-End** test on this service I recommend use the [Postman][]

[Postman]: https://www.getpostman.com/

## Deployment

To create standalone artifact (.jar)

    lein ring uberjar

To run standalone artifact (need Java JRE)

    java -jar target/account-1.0.0.jar

> See all releases of this project [here][]!

[here]: https://github.com/chinnonsantos/account-service/releases

## License

Copyright © 2019 | Chinnon Santos | Apache License 2.0
