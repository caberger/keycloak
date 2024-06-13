#!/usr/bin/env bash

# to use this script backup container must be running, so start the following before:
# docker compose -f backup-keycloak.yml up

mkdir -p ./target
docker compose exec postgres pg_dump --dbname=keycloak --username=keycloak | gzip > ./target/keycloak.sql.gz
docker compose cp backup:/export ./target
