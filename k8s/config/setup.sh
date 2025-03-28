#!/usr/bin/env bash

# Check if cluster is running
kubectl get pods
if [ $? -ne 0 ]; then
  echo "Kubernetes is not running and failed with exit status $?"
  exit 1
else
  echo "Kubernetes is running"
fi

# Check if pg secrets are present
if [[ -z "${CACOPHONY_DB_USERNAME}" ]]; then
    echo "CACOPHONY_DB_USERNAME environment variable is not present, exiting"
    exit 1
fi

if [[ -z "${CACOPHONY_DB_PASSWORD}" ]]; then
    echo "CACOPHONY_DB_PASSWORD environment variable is not present, exiting"
    exit 1
fi

