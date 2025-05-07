#!/usr/bin/env bash


#/opt/keycloak/bin/kc.sh export --db postgres --db-url-host postgres --db-url-database jdbc:postgresql://postgres/keycloak --db-username keycloak --db-password keycloak --realm demo --dir /opt/keycloak/export
set -e

docker compose exec keycloak /opt/keycloak/bin/kc.sh export --db postgres --db-url jdbc:postgresql://postgres/keycloak \
    --db-username keycloak --db-password keycloak \
    --realm demo --dir /opt/keycloak/export --users same_file

DIR=~/Downloads/keycloak
mkdir -p $DIR
rm -f $DIR/*

FILES=$(docker compose exec keycloak ls /opt/keycloak/export/)
for file in $FILES
do
    docker compose cp keycloak:/opt/keycloak/export/$file $DIR
#    jq '.realm' $DIR/$file
done
echo "files exported to $DIR"
ls -l $DIR
