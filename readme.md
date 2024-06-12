# Authentication and Authorization in a Web-App using keycloak

This example shows how to secure a simple Web-Application using a REST - API protected with [Json Web Tokens](https://jwt.io/)


## Frontend

The frontend uses lit-html to show a simple main page that displays user details and user roles from the keycloak authentication service and a greeting fetched from the backend that is secured by json web tokens received from keycloak

## Backend

The backend is a simple ORM Rest - Api implemented in rust

## Authentication and Authorization server

The keycloak server implements the user database and returns Json Web Tokens on successfull authentication. As a backend a postgresql database is used.

## Database

We use postgresql as database. This server contains two databases. The "demo" database contains demo data for the backend. The "keycloak" database is the storage for keycloak.


## Building

Building is supported on Linux and OSX.

You the follwing build tools:
- [nodejs](https://nodejs.org/)
- [cargo](https://doc.rust-lang.org/stable/cargo/index.html)
- [diesel](https://diesel.rs/)


To build the docker images run the following:
```bash
./build.sh
```
This should start a docker compose postgres postgres database, wait until it is available, 
generate the ORM schema.rs mapping file and create the database tables and then start the builds of our docker images.

to clean all images you can use ```./compose/clean-docker.sh``` before the build.

to start the system change to the compose folder and run the following:
```bash
cd compose
./start.sh
```
The service - startup procedure can be seen with ```watch docker compose ps```.
The services have health checks included, so dependent services only start when the services they need are healthy. Wait until all four services are healthy.

## Adding users to keycloak

to create the demo realm import the file ./components/keycloak/setup/realm-export.json in the keycloak admin panel. You can find the keycloak username and password in the docker-compose.yaml file.

## delete all docker containers, images and volumes

```bash
./clean-docker.sh
```

## backup the database

```bash
docker compose exec postgres -- pg_dump -U demo
docker compose exec postgres -- pg_dump -U keycloak
```

## Development using a local docker compose database

For development it is necessary to build and run the docker images, see Building above


To start the development containers use the following command:
```bash
cd ./compose
docker compose -f dev.yaml up --build
```

Then you can start the application server with:
```bash
cd ./backend
cargo run
```
and the frontend with:

```bash
cd ./frontend
npm install
npm start
```



