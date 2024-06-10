#!/usr/bin/env bash

set -eux
docker ps
pushd ./backend
    docker build --tag builder --file Dockerfile.builder
    docker build --tag backend .
popd
pushd ./frontend
    npm install
    npm run build
popd
pushd ./compose
    pushd keycloak
        docker build -t keycloak .
    popd
popd

