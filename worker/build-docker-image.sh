#!/bin/bash

ecrRepository=$1
imageTag=$2

./mvnw clean package

docker buildx build --platform linux/amd64 -t $ecrRepository:$imageTag .