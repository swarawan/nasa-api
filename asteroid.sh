#!/bin/bash

# backward compatibility with older docker compose version
dockerCompose() {
  if command -v docker-compose &> /dev/null
  then
    docker-compose $@
  else
    docker compose $@
  fi
}

# Clean unused docker image.
cleanFunction() {
    none_images=`docker images | grep "^<none>" | awk '{ print $3 }'`
    echo $none_images
    if [ ! -z "$none_images" ]; then
      docker rmi $(docker images | grep "^<none>" | awk '{ print $3 }')
    fi
    exit 1
}

runFunction() {
  dockerCompose up -d nasa-asteroid
  cleanFunction
  exit 1
}

buildFunction() {
  dockerCompose build $build_args $service_name
  exit 1
}

installFunction() {
  dockerCompose up -d "$1"
}

case $1 in
  build) buildFunction;;
  run) runFunction;;
  clean) cleanFunction;;
  install) installFunction;;
  *) echo "Command not found :( ";;
esac