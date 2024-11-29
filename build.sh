#!/usr/bin/env bash

set -e

docker ps

pushd ./frontend
    npm install
    npm run build
    docker build --tag www --file docker/Dockerfile .
popd
pushd ./application-server
. ./build.sh
popd

pushd ./compose    
    docker compose -f postgres.yaml pull
    pushd ./keycloak
        docker build --tag keycloak --platform linux/amd64 .
    popd
popd

echo "==="
echo "= the java backend in application-server haas been built"
echo "= to build the rust backend please change to the backend folder and run the build.sh file in that folder"
echo "==="
