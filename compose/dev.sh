#!/usr/bin/env bash

pushd keycloak
docker build --tag keycloak .
popd
docker compose -f dev.yaml up --build
