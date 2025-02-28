#!/usr/bin/env bash

DESTINATION=./target/export
set -e

while ! nc -z localhost 5432; do
    echo "waiting for docker compose up..."
    sleep 1
done

rm -rf $DESTINATION
mkdir -p $DESTINATION
docker compose --file backup.yaml up --build
cp $DESTINATION/*.json ./keycloak/import

docker compose exec postgres pg_dump --dbname=keycloak --username=keycloak | gzip > $DESTINATION/keycloak.sql.gz
docker compose exec postgres pg_dump --dbname=demo --username=demo | gzip > $DESTINATION/demo.sql.gz

