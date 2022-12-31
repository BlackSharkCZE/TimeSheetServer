#!/usr/bin/env bash

SCRIPT_DIR="$(dirname $0)"
cd "$SCRIPT_DIR"
unset NODE_OPTIONS

if ./mvnw clean package;
  cd "target/quarkus-app/"
  tar cfz app.tgz app/ lib/ quarkus/ quarkus-app-dependencies.txt quarkus-run.jar
  scp app.tgz root@jiri-pejsa.cz://tmp
then
  echo "Create release build failed"
fi


exit 0
