#!/usr/bin/env bash

set -e

# to use this script backup container must be running, so start the following before:
docker compose --detach --file backup-keycloak.yaml up

echo "Waiting  to launch on 8080..."

while ! nc -z localhost 8080; do   
  sleep 0.1 # wait for 1/10 of the second before check again
done
mkdir -p ./target
docker compose exec postgres pg_dump --dbname=keycloak --username=keycloak | gzip > ./target/keycloak.sql.gz
docker compose cp backup:/export ./target

cp ./target/export/*.json ./keycloak/import

docker compose --file backup-keycloak.yaml down
