#!/bin/zsh

./mvnw clean package

docker build -t worker:latest .