#!/usr/bin/env bash

echo "start and wait for postgresql database"
pushd ../compose
    docker compose -f postgres.yaml up --build --detach
    while ! docker compose -f postgres.yaml exec postgres psql --dbname=demo --username=demo -c "select 'yes' as database_available";
    do   
      echo "wait for database to be ready ..."
      sleep 1
    done
    echo "database is available, try to create tables and generate file schema.rs..."
popd

echo "create database tables and schema.rs..."
diesel migration run # create tables and schema

pushd ./compose
    docker compose -f postgres.yaml down
popd

docker build -f Dockerfile.builder --tag builder .
docker build --tag backend .
