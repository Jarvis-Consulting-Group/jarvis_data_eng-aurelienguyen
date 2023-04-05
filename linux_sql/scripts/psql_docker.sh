#!/bin/bash

cmd=$1
db_username=$2
db_password=$3

# start docker if docker server is not running
sudo systemctl status docker || sudo systemctl start docker

# get container status (0 if container exists)
docker container inspect jrvs-psql
container_status=$?

# switch case for create|start|stop commands
case $cmd in
  create)

  if [ $container_status -eq 0 ]; then
		echo 'Container already exists'
		exit 1
	fi

  # need 3 CLI arguments to create container
  if [ $# -ne 3 ]; then
    echo 'Create requires username and password'
    exit 1
  fi

  # create container
	docker volume create pgdata
  # start the container
	docker run --name jrvs-psql -e POSTGRES_USERNAME=db_username -e POSTGRES_PASSWORD=db_password -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres:9.6-alpine
  # exit value of last executed command
	exit $?
	;;

  start|stop)
  # check if container exists
  if [ $container_status -ne 0 ]; then
    echo 'Container does not exist'
    exit 1
  fi

  # start or stop the container
	docker container $cmd jrvs-psql
	exit $?
	;;

  *)
	echo 'Illegal command'
	echo 'Commands: start|stop|create'
	exit 1
	;;
esac