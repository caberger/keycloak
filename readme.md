# Securing a Web-App using Keycloak

This example shows how to secure a simple Web-Application using a REST - API that is protected with [Json Web Tokens](https://jwt.io/) using [keycloak](https://www.keycloak.org/).

## Nodbms

Let me site the Eclipse Store Website:

*A DataBase Management System is something of a server application platform of an "old kind" on top of its data store functionality: A standalone process with user management, connection management, session handling, often even with a programming language of its own, a querying interface (SQL), etc. Today, all of those server application features are already and much better handled by dedicated server applications (the "new kind"), implemented in a modern language like Java. They have their built-in user, connection and session management, the querying interface to the outside world are typically web services instead of SQL, etc. But those modern server applications still lack one important thing: an easy to use and technically efficient way to store and restore their application’s data. So a "new kind" server often uses an "old kind" server just to do the data storing. This comes at the price of catching all the overhead and problems of redundant user, connection and session management AND the outdated concepts and limitations of the old querying interface (SQL).*

So, in this project we do not use outdated DBMS technology that was designed for computers that lived 50 years ago. We use a storage that conforms to the [Single-responsibility principle](https://en.wikipedia.org/wiki/Single-responsibility_principle).

## Frontend

The frontend uses lit-html to show a simple main page that displays user details and user roles from the keycloak authentication service and a greeting fetched from the backend that is secured by json web tokens received from keycloak

## Alternatives

The alternatives/rust folder is a simple ORM Rest - Api implemented in rust (deprecated).
The vintage-frontend folde is a angular application showing how to use keycloak (deprecated),

## backend

backend contains an application-server implemented with quarkus. 

## Authentication and Authorization server

The keycloak server implements the user database and returns Json Web Tokens on successful authentication. As a backend a postgresql database is used.

## Database

The "keycloak" database is the storage for keycloak.

We use [eclipse store](https://eclipsestore.io/) as database for the backend and support backup as well as export/import to xlsx (Libreoffice Calc and/or Excel format). 
For motivation see [Storage](https://docs.eclipsestore.io/manual/storage/index.html)

## Building

Docker must be started and clean. To make sure that no old caches and databases exist run the following:
```bash
./compose/clean-docker.sh
```

Builds are supported on Linux and OSX.

You the follwing build tools:
- [nodejs](https://nodejs.org/)
- maven

To build the docker images run the following:
```bash
./start.sh
```


to start the system without full compile change to the compose folder and run the following:
```bash
cd compose
./start.sh
```
The services have health checks included, so dependent services only start when the services they need are healthy. Wait until all four services are healthy.

## Adding users to keycloak

to create the demo realm import the file ./components/keycloak/setup/realm-export.json in the keycloak admin panel. You can find the keycloak username and password in the docker-compose.yaml file.

## delete all docker containers, images and volumes

to clear the cache and clean all images you can use ```./compose/clean-docker.sh``` before the build.

## backup the database

```bash
docker compose exec postgres -- pg_dump -U keycloak
```

## Testing

Go to [api.rest](./api/requests) and run:
```bash
npm install
npm test
```

## Performance and Import/Export

### startup: inital random data
On startup records are created.
You can adjust the number of records created at startup in by changing *store.create.number-of-test-records* property in [application.properties](./code/application-server/src/main/resources/application.properties)

### Import/Export

You can export and import the database to a LibreOffice/Excel workbook with the import/export buttons in the frontend.
Note the performance 😲🤩
