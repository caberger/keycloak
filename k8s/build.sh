#!/usr/bin/env bash

pushd keycloak
    docker build -t keycloak .
popd
pushd ../backend
    docker build -t backend .
popd