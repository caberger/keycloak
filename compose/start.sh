#!/usr/bin/env bash

docker compose up --build --detach

docker compose ps
while [ $(docker compose ps | grep keycloak | grep healthy | wc -l) -lt 1 ]
do
    docker compose ps
    echo "⏳..."
    sleep 2
done
docker compose logs
docker compose ps

echo "👍"
