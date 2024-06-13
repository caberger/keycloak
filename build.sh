#!/usr/bin/env bash

set -eux
docker ps

pushd ./compose    
    pushd ./keycloak
        docker build --tag keycloak .
    popd
    docker compose -f postgres.yaml pull
popd
pushd ./frontend
    npm install
    npm run build
    docker build --tag www --file docker/Dockerfile .
popd

pushd ./compose
#    ./clean-docker.sh || echo "noting to clean"
    docker compose -f postgres.yaml up --build --detach
    while ! docker compose -f postgres.yaml exec postgres psql --dbname=demo --username=demo -c "select 'yes' as database_available";
    do   
      echo "wait for database to be ready ..."
      sleep 1
    done
    echo "database is available, try to create tables and generate file schema.rs..."
popd
pushd ./backend
    echo "create schema and create schema.rs..."
    diesel migration run # create tables and schema
popd
pushd ./compose
    docker compose -f postgres.yaml stop
popd
pushd ./backend
    docker build -f Dockerfile.builder --tag builder .
    docker build --tag backend .
popd
