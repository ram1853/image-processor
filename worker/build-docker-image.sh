#!/bin/zsh

./mvnw clean package

docker buildx build --platform linux/amd64,linux/arm64 -t image-processor:1.0.9 .