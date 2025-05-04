#!/usr/bin/env bash

set -e

pushd compose
    docker ps
    docker compose down
popd 
