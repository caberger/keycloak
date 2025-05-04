#!/usr/bin/env bash

set -e

docker ps

pushd ./frontend
    echo "install frontend dependencies..."
    npm install 2>&1 > /dev/null
    npm audit --omit dev
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
echo "= 1.) open a new terminal in $PWD/backend and run: ./start.sh"
echo "= 2.) open a new terminal in $PWD/frontend and run: npm start"
echo "==="
