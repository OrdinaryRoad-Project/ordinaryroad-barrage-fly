# barrage-fly 让弹幕飞

一个弹幕转发、过滤、处理平台

[在线文档](https://barragefly.ordinaryroad.tech)

## 1 项目简介

### 1.1 功能

1. 监听不同平台直播间的弹幕
2. 使用统一的协议将弹幕转发出去
3. 内置简易实时弹幕显示

| <img src="docs/src/.vuepress/public/assets/image/task.png" width="400"/>              | <img src="docs/src/.vuepress/public/assets/image/task-detail-1.png" width="400"/> | <img src="docs/src/.vuepress/public/assets/image/task-detail-2.png" width="400"/> |
|---------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------|-----------------------------------------------------------------------------------|
| <img src="docs/src/.vuepress/public/assets/image/real-time-barrage.png" width="400"/> | <img src="docs/src/.vuepress/public/assets/image/stats-1.png" width="400"/>       | <img src="docs/src/.vuepress/public/assets/image/stats-2.png" width="400"/>       |

### 1.2 作用

屏蔽不同平台直播平台弹幕协议的差异，便于开发其他与弹幕相关的应用：实时弹幕大屏、弹幕互动、弹幕存档、弹幕分析、弹幕词云图等

### 1.3 相关技术栈

- [Netty](https://netty.io)
- [Spring WebFlux](https://docs.spring.io/spring-framework/reference/web/webflux.html)
- [RSocket](https://rsocket.io)
- [QLExpress](https://github.com/alibaba/QLExpress)

## 2 项目部署

项目前后端分离，可以使用Docker Compose进行部署，或者分别打包后部署

### 2.1 Docker Compose

> Docker Compose示例位于`.docker`目录中

- arm64系统时，后端镜像请使用 `ordinaryroad-barrage-fly-arm64`
- 使用mysql、nginx时需要先build
    - `docker compose build ordinaryroad-barrage-fly-mysql`
    - `docker compose build ordinaryroad-barrage-fly-nginx`
- 在线生成RSA密钥对：https://www.bejson.com/enc/rsa/ （不需要-----BEGIN PUBLIC KEY-----）

> 镜像加速与拉取
> ```shell
> # DOCKER_OPTS="--registry-mirror=https://mirror.ccs.tencentyun.com"
> docker pull ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly:$APP_VERSION
> docker pull ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-arm64:$APP_VERSION
> docker pull ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-ui:$APP_VERSION
> ```

## 其他

- [ordinaryroad-live-chat-client](https://github.com/OrdinaryRoad-Project/ordinaryroad-live-chat-client)

## 免责声明

免责声明：仅供学术研究使用。对于违反相关法律、造成危害的滥用行为，开发者不负任何责任。
