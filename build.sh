#!/usr/bin/env bash
#set -eux

set -e

docker ps

pushd ./compose    
    docker compose -f postgres.yaml pull
    pushd ./keycloak
        docker build --tag keycloak --platform linux/amd64 .
        docker tag keycloak ghcr.io/htl-leo-itp-2325-4-5bhitm/demo-keycloak:latest
        docker login ghcr.io
        docker push ghcr.io/htl-leo-itp-2325-4-5bhitm/demo-keycloak:latest
    popd
popd
pushd ./frontend
    npm install
    npm run build
    docker build --tag www --file docker/Dockerfile .
popd

echo "==="
echo "= to build the rust backend please change to the backend folder and run the build.sh file in that folder"
echo "==="
