# timesheet-server2 Project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

### TODO

* `07.12.2022`: ( SOLVED ) Execute remote writer does not mark row as wrote without table refresh
* `08.12.2022`: ( SOLVED ) Create script to database backup.
* `08.12.2022`: ( SOLVED ) Pridat zpetnou vazbu pri zapisu vykazu do vzdaleneho systemu
* `09.12.2022`: ( SOLVED ) Spatne detekovany prekryv casu, kdyz jsou to ruzne projekty (chyba dat v databazi - pridane vteriny u timestammp)
* `09.12.2022`: ( SOLVED )Editace timeline
* `09.12.2022`: ( SOLVED ) Spatne informace v summary v prvem hornim rohu
* `13.12.2022`: ( SOLVED )Add valid keycloak configuratio during build (dev and remote) 
* `14.12.2022`: ( SOLVED ) Add remote writer does not send all required data
* `14.12.2022`: ( SOLVED? ) Do GEM timesheet je spatne zapsany cas (posunuty o hodinu)
* `14.12.2022`: Volani REST rozhrani pro remote write vraci http status 500, nezobrazi chybu a data jsou zapsana: ListTimelineTable.vue:130
* `15.12.2022`: ( SOLVED ) Pri zavreni editacniho okna radku pomoci krizku vpravo nahore, neni jiz mozne dalsi okno otevrit
* `16.12.2022`: ( SOLVED ) Update timeline by mel vracet cely radek tak jak je vraceny ze zdroje pro tabulku

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

## running the application with vue.js watch
```shell script
./mvnw exec:exec@npm-watch quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package

```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/timesheet-server2-2.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.

## Related Guides

- YAML Configuration ([guide](https://quarkus.io/guides/config#yaml)): Use YAML to configure your Quarkus application
- RESTEasy JAX-RS ([guide](https://quarkus.io/guides/rest-json)): REST endpoint framework implementing JAX-RS and more
- Kotlin ([guide](https://quarkus.io/guides/kotlin)): Write your services in Kotlin

## Provided Code

### YAML Config

Configure your application with YAML

[Related guide section...](https://quarkus.io/guides/config-reference#configuration-examples)

The Quarkus application configuration is located in `src/main/resources/application.yml`.

### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)
