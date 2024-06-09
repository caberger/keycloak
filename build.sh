#!/usr/bin/env bash

set -eux
docker ps
pushd ./backend
    docker build -t backend .
popd
pushd ./frontend
    npm install
    npm run build
popd
pushd compose
    pushd keycloak
        docker build -t keycloak .
    popd
popd
