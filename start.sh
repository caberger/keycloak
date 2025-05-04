#!/usr/bin/env bash

bold=$(tput bold)
normal=$(tput sgr0)

set -e

docker ps

pushd compose
    docker compose down
popd

pushd ./frontend
    echo "install frontend dependencies..."
    npm install 2>&1 > /dev/null
    npm audit --omit dev
    npm run build
popd

pushd ./backend
    echo "install backend dependencies..."
    mvn dependency:resolve
popd

pushd ./compose
    echo "start docker compose..."
    bash -c ./start.sh
popd

echo "==="
echo "= docker compose is started."
echo "= 1.) open a new terminal in $PWD/backend and run: → ${bold}./start.sh${normal}"
echo "= you can start the application at http://localhost:8080"
echo "= 2.) for frontend development on http://localhost:4200 you can open a new terminal in $PWD/frontend and run: → ${bold}npm start${normal}"
echo "==="
