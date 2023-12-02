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
export APP_VERSION=1.0.6

# 1 后端
cd ../barrage-fly
./gradlew clean bootJar
cd ../.docker/ordinaryroad-barrage-fly
docker build . -f Dockerfile -t ordinaryroad-barrage-fly:${APP_VERSION}
docker tag ordinaryroad-barrage-fly:${APP_VERSION} ordinaryroad-barrage-fly
docker build . -f Dockerfile-arm64 -t ordinaryroad-barrage-fly-arm64:${APP_VERSION}
docker tag ordinaryroad-barrage-fly-arm64:${APP_VERSION} ordinaryroad-barrage-fly-arm64

# 2 前端
cd ../../barrage-fly-ui
npm run copy
cd ../.docker/ordinaryroad-barrage-fly-ui/app
npm install
npm run build
cd ..
docker build . -f Dockerfile -t ordinaryroad-barrage-fly-ui:${APP_VERSION}
docker tag ordinaryroad-barrage-fly-ui:${APP_VERSION} ordinaryroad-barrage-fly-ui