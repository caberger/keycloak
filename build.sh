#!/usr/bin/env bash

set -e

docker ps

pushd ./frontend/frontend
    npm install
    npm run build
    docker build --tag www --file docker/Dockerfile .
popd
pushd ./backend/quarkus
. ./build.sh
popd

pushd ./compose
    docker compose -f postgres.yaml pull
    pushd ./keycloak
        docker build --tag keycloak --platform linux/amd64 .
    popd
    docker compose up --build --detach
    docker compose ps
popd

echo "==="
echo "= the java backend have been built."
echo "= docker compose is started."
echo "= Now change to backend/quarkus and run mvn quarkus:dev"
echo "= then change to frontend/frontend and run npm start"
echo "= to build the rust backend please change to the backend/rust folder and run the build.sh file in that folder"
echo "==="
