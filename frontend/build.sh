#!/usr/bin/env bash

set -e

echo "check for jq installation"
jq --version || (echo "please install js, see https://jqlang.org/" && exit 1)
npm install --no-fund --no-audit
npm audit --omit dev

OUTDIR=$(npx tsc --showConfig | jq -r '.compilerOptions.outDir')
npm run build

cp -r $OUTDIR/* ../backend/src/main/resources/META-INF/resources/
