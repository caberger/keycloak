#!/usr/bin/env bash

set -e

npm install --no-fund --no-audit
npm audit --omit dev

npm run build
DEST=../backend/application-server/src/main/resources/META-INF/resources/
mkdir -p $DEST
cp -r ./dist/ $DEST
cp -r ./html $DEST
echo "frontend deployed to $DEST"
