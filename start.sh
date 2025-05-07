#!/usr/bin/env bash

bold=$(tput bold)
normal=$(tput sgr0)

set -e

docker ps

pushd ./code/compose
    docker compose down
popd

pushd ./code/frontend
    ./build.sh
popd

pushd ./code/backend/application-server
    echo "install backend dependencies..."
    mvn dependency:resolve
popd

pushd ./code/compose
    echo "start docker compose..."
    bash -c ./start.sh
popd

echo "==="
echo "= docker compose started."
echo "= 1.) open a new terminal in $PWD/code/application-server and run: → ${bold}./start.sh${normal}"
echo "= you can start the application at http://localhost:8080"
echo "= 2.) for frontend development on http://localhost:4200 you can open a new terminal in $PWD/code/frontend and run: → ${bold}npm start${normal}"
echo "==="
