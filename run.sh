#!/bin/bash
set -eo pipefail
mvn clean package
docker-compose up -d --build