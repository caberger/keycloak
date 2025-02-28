#!/usr/bin/env bash

set -e

mkdir -p ./target

echo "Waiting for postgres..."

while ! nc -z localhost 5432; do
    echo "wait for postgres..."
    sleep 1
done

docker compose exec postgres pg_dump --dbname=keycloak --username=keycloak | gzip > ./target/keycloak.sql.gz
docker compose exec postgres pg_dump --dbname=demo --username=demo | gzip > ./target/demo.sql.gz

echo "backup done, see $PWD/target"
ls -l ./target

