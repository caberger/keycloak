#!/usr/bin/env bash

set -eux
docker ps

pushd ./compose
#    ./clean-docker.sh || echo "noting to clean"
    docker compose -f postgres-compose.yaml pull
    docker compose -f postgres-compose.yaml up --build --detach
    while ! docker compose -f postgres-compose.yaml exec postgres pg_isready --dbname=demo --username=demo;
    do   
      echo "wait for dataabase to be ready ..."
      sleep 1
    done
    echo "database is available"
popd
pushd ./backend
    echo "create schema and create schema.rs..."
    diesel migration run # create tables and schema
popd
pushd ./compose
    docker compose -f postgres-compose.yaml stop
popd
pushd ./backend
    docker build -f Dockerfile.builder --tag builder .
    docker build --tag backend .
popd
pushd ./compose    
    pushd ./keycloak
        docker build --tag keycloak .
    popd
popd
pushd ./frontend
    npm install
    npm run build
    docker build --tag www --file docker/Dockerfile .
popd

