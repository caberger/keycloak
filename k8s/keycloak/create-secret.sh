#!/usr/bin/env bash


kubectl delete secret keycloak-credentials || echo "secrets do not yet exit"
kubectl create secret generic keycloak-credentials --from-env-file=keycloak-credentials.properties


