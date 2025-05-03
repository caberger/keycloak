#!/usr/bin/env bash

set -e

docker ps

pushd ./frontend/web-components
    npm install
    npm run build
#    docker build --tag www --file docker/Dockerfile .
popd
pushd ./backend/quarkus
. ./build.sh
popd

pushd ./compose
    ./start.sh
popd

echo "==="
echo "= the java backend have been built."
echo "= docker compose is started."
echo "= now open a new terminal in $PWD/backend/quarkus and run: mvn quarkus:dev"
echo "= then open a new terminal in $PWD/frontend/web-components and run: npm start"
#echo "= to build the rust backend please change to the backend/rust folder and run the build.sh file in that folder"
echo "==="
