#!/bin/bash

#
# Copyright 2023 OrdinaryRoad
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
export PWD_PATH=$(pwd)

docker login -u cnb docker.cnb.cool

export APP_VERSION=1.6.4

# 1 后端
cd ../barrage-fly
rm -rf ./src/main/resources/public/*
rm -rf ./src/main/resources/static/*
./gradlew clean bootJar
cd ../.docker/ordinaryroad-barrage-fly

# 构建并发布多平台版本
docker buildx build --platform linux/arm64,linux/amd64 --push . -f Dockerfile -t docker.cnb.cool/ordinaryroad/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly:${APP_VERSION}
docker buildx build --platform linux/arm64,linux/amd64 --push . -f Dockerfile -t docker.cnb.cool/ordinaryroad/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly

# 更新本地镜像
docker pull docker.cnb.cool/ordinaryroad/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly:${APP_VERSION}
docker tag docker.cnb.cool/ordinaryroad/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly:${APP_VERSION} ordinaryroad-barrage-fly:${APP_VERSION}
docker pull docker.cnb.cool/ordinaryroad/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly
docker tag docker.cnb.cool/ordinaryroad/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly ordinaryroad-barrage-fly


# 2 前端
cd ../../barrage-fly-ui
npm run copy
cd ../.docker/ordinaryroad-barrage-fly-ui/app
npm install
npm run build
cd ..

# 构建并发布多平台版本
docker buildx build --platform linux/arm64,linux/amd64 --push . -f Dockerfile -t docker.cnb.cool/ordinaryroad/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-ui:${APP_VERSION}
docker buildx build --platform linux/arm64,linux/amd64 --push . -f Dockerfile -t docker.cnb.cool/ordinaryroad/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-ui

# 更新本地镜像
docker pull docker.cnb.cool/ordinaryroad/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-ui:${APP_VERSION}
docker tag docker.cnb.cool/ordinaryroad/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-ui:${APP_VERSION} ordinaryroad-barrage-fly-ui:${APP_VERSION}
docker pull docker.cnb.cool/ordinaryroad/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-ui
docker tag docker.cnb.cool/ordinaryroad/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-ui ordinaryroad-barrage-fly-ui

#docker logout docker.cnb.cool

# docs
cd ${PWD_PATH}
cd ../.docker/ordinaryroad-barrage-fly-docs
docker buildx build --platform linux/arm64,linux/amd64 --push . -f Dockerfile -t docker.cnb.cool/ordinaryroad/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-docs:${APP_VERSION}
docker buildx build --platform linux/arm64,linux/amd64 --push . -f Dockerfile -t docker.cnb.cool/ordinaryroad/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-docs

echo "deploy finished, please refer to https://cnb.cool/ordinaryroad/ordinaryroad-barrage-fly/docker-pub/-/packages?type=docker&ordering=last_push_at"