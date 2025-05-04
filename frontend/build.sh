#!/usr/bin/env bash

set -e

npm install --no-fund --no-audit
npm audit --omit dev

npm run build
cp -r ./dist/ ../backend/src/main/resources/META-INF/resources/
cp -r ./html ../backend/src/main/resources/META-INF/resources/
