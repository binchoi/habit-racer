#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

function switch_proxy() {
  IDLE_PORT=$(find_idle_port)

  echo "> Port to switch to: $IDLE_PORT"
  echo "> Switching Port"
  echo "set \$service_url https://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc

  echo "> ngnix reload"
  sudo service nginx reload
}