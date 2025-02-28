#!/usr/bin/env bash

set -e
mkdir -p ./target/export/

# to use this script backup container must be running, so start the following before:
docker compose --file backup-keycloak.yaml up --build --detach
#docker compose exec postgres pg_dump --dbname=keycloak --username=keycloak | gzip > ./target/keycloak.sql.gz
docker compose --file backup-keycloak.yaml down
echo "TODO: cp ./target/export/*.json ./keycloak/import"
