#!/usr/bin/env sh

nvm use 11 && \
./mvnw clean exec:exec@npm-watch quarkus:dev
exit 0
