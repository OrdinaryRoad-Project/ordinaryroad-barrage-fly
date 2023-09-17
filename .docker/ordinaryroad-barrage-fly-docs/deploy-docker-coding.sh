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

cd /root/ordinaryroad/${parameters.APP_NAME}

docker login -u ${parameters.DOCKER_U} -p ${parameters.DOCKER_P} ordinaryroad-docker.pkg.coding.net

docker pull ordinaryroad-docker.pkg.coding.net/${parameters.APP_NAME}/docker-pub/${parameters.APP_NAME}:${parameters.APP_VERSION}

docker logout

docker rmi ${parameters.APP_NAME} --force

docker tag ordinaryroad-docker.pkg.coding.net/${parameters.APP_NAME}/docker-pub/${parameters.APP_NAME}:${parameters.APP_VERSION} ${parameters.APP_NAME}

docker compose up -d ${parameters.APP_NAME}