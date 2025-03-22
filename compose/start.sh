#!/usr/bin/env bash

docker compose up --build --detach
watch --no-title --no-wrap docker compose ps
