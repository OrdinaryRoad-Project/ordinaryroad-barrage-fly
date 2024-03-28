# 2 项目部署

项目前后端分离，可以使用Docker Compose进行部署，或者clone项目到本地后分别打包后部署

演示DEMO：https://barragefly.ordinaryroad.tech:7000，帐号请前往[QQ频道](https://pd.qq.com/s/3id0n7fvs)获取

WebSocket地址：wss://barragefly.ordinaryroad.tech:7100

## 2.1 Docker Compose

> Docker Compose示例位于`.docker`目录中

- arm64系统时，后端镜像请使用 `ordinaryroad-barrage-fly-arm64`
- 使用mysql、nginx时需要先build
    - `docker compose build ordinaryroad-barrage-fly-mysql`
    - `docker compose build ordinaryroad-barrage-fly-nginx`
- 在线生成RSA密钥对：https://www.bejson.com/enc/rsa/ （不需要第一行的`-----BEGIN XXXXX KEY-----`和最后一行的`-----END XXXXX KEY-----`）

> 镜像加速与拉取：[仓库链接](https://ordinaryroad.coding.net/public-artifacts/ordinaryroad-barrage-fly/docker-pub/packages)
>
> ```shell
> # DOCKER_OPTS="--registry-mirror=https://mirror.ccs.tencentyun.com"
> # 拉取最新版1.1.5
> docker pull ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly:1.1.5
> docker pull ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-arm64:1.1.5
> docker pull ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-ui:1.1.5
> ```

> 拉到本地后需要根据compose文件重命名镜像
> ```shell
> docker tag ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly:1.1.5 ordinaryroad-barrage-fly
> docker tag ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-arm64:1.1.5 ordinaryroad-barrage-fly-arm64
> docker tag ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-ui:1.1.5 ordinaryroad-barrage-fly-ui
> ```

### 2.1.1 前端+后端

```yaml
# 需要的环境变量，其他可以环境变量可看每个目录下的.env文件，初始化sql位于`.docker/ordinaryroad-barrage-fly-mysql/sql/or_barrage_fly.sql`
# MYSQL_HOST（注意请勿用localhost、127.0.0.1）
# MYSQL_PORT
# MYSQL_DATABASE
# MYSQL_USERNAME
# MYSQL_PASSWORD
# SUB_BASE_URL（根据后端部署情况设置Client要连接的地址）
# SPRING_BOOT_ADMIN_BASE_URL（根据后端部署情况设置Spring Boot Admin地址）
# RSA_PUBLIC_KEY（RSA）
# ADMIN_USERNAME（任务管理后台登录用户名）
# ADMIN_PASSWORD（任务管理后台登录密码）
# RSA_PRIVATE_KEY（RSA）

version: "3.0"
services:
  ordinaryroad-barrage-fly-ui:
    image: ordinaryroad-barrage-fly-ui
    container_name: ordinaryroad-barrage-fly-ui
    volumes:
      - $PWD/ordinaryroad-barrage-fly-ui/app:/ordinaryroad/ordinaryroad-barrage-fly-ui/app
    environment:
      BASE_URL: http://ordinaryroad-barrage-fly:8080
      RSA_PUBLIC_KEY:
      SUB_BASE_URL:
    ports:
      - "30000:3000"
    hostname: ordinaryroad-barrage-fly-ui
    restart: always

  ordinaryroad-barrage-fly:
    image: ordinaryroad-barrage-fly
    container_name: ordinaryroad-barrage-fly
    environment:
      MYSQL_HOST:
      MYSQL_PORT:
      MYSQL_DATABASE:
      MYSQL_USERNAME:
      MYSQL_PASSWORD:
      ADMIN_USERNAME:
      ADMIN_PASSWORD:
      RSA_PUBLIC_KEY:
      RSA_PRIVATE_KEY:
    ports:
      - "8080:8080"
      - "9898:9898"
    hostname: ordinaryroad-barrage-fly
    restart: always
```

### 2.1.2 前端+后端+MySQL

```yaml
# 需要的环境变量，其他可以环境变量可看每个目录下的.env文件
# MYSQL_ROOT_PASSWORD（MySQL初始ROOT密码）
# SUB_BASE_URL（根据后端部署情况设置Client要连接的地址）
# SPRING_BOOT_ADMIN_BASE_URL（根据后端部署情况设置Spring Boot Admin地址）
# RSA_PUBLIC_KEY（RSA）
# ADMIN_USERNAME（任务管理后台登录用户名）
# ADMIN_PASSWORD（任务管理后台登录密码）
# RSA_PRIVATE_KEY（RSA）

version: "3.0"
services:
  ordinaryroad-barrage-fly-mysql:
    image: ordinaryroad-barrage-fly-mysql
    container_name: ordinaryroad-barrage-fly-mysql
    build:
      context: ./ordinaryroad-barrage-fly-mysql
    volumes:
      - $PWD/ordinaryroad-barrage-fly-mysql/etc/my.cnf:/etc/my.cnf
      - $PWD/ordinaryroad-barrage-fly-mysql/data:/var/lib/mysql
    environment:
      MYSQL_ROOT_PASSWORD:
    ports:
      - "33066:3306"
    hostname: ordinaryroad-barrage-fly-mysql
    restart: always

  ordinaryroad-barrage-fly-ui:
    image: ordinaryroad-barrage-fly-ui
    container_name: ordinaryroad-barrage-fly-ui
    volumes:
      - $PWD/ordinaryroad-barrage-fly-ui/app:/ordinaryroad/ordinaryroad-barrage-fly-ui/app
    environment:
      BASE_URL: http://ordinaryroad-barrage-fly:8080
      RSA_PUBLIC_KEY:
      SUB_BASE_URL:
    ports:
      - "30000:3000"
    hostname: ordinaryroad-barrage-fly-ui
    restart: always

  ordinaryroad-barrage-fly:
    image: ordinaryroad-barrage-fly
    container_name: ordinaryroad-barrage-fly
    environment:
      MYSQL_USERNAME: root
      MYSQL_PASSWORD: ${MYSQL_ROOT_PASSWORD}
      ADMIN_USERNAME:
      ADMIN_PASSWORD:
      RSA_PUBLIC_KEY:
      RSA_PRIVATE_KEY:
    ports:
      - "8080:8080"
      - "9898:9898"
    hostname: ordinaryroad-barrage-fly
    restart: always
```

### 2.1.3 前端+后端+MySQL+Nginx

```yaml
# 需要的环境变量，其他可以环境变量可看每个目录下的.env文件
# MYSQL_ROOT_PASSWORD（MySQL初始ROOT密码）
# SUB_BASE_URL（根据后端部署情况设置Client要连接的地址）
# SPRING_BOOT_ADMIN_BASE_URL（根据后端部署情况设置Spring Boot Admin地址）
# RSA_PUBLIC_KEY（RSA）
# ADMIN_USERNAME（任务管理后台登录用户名）
# ADMIN_PASSWORD（任务管理后台登录密码）
# RSA_PRIVATE_KEY（RSA）

version: "3.0"
services:
  ordinaryroad-barrage-fly-mysql:
    image: ordinaryroad-barrage-fly-mysql
    container_name: ordinaryroad-barrage-fly-mysql
    build:
      context: ./ordinaryroad-barrage-fly-mysql
    volumes:
      - $PWD/ordinaryroad-barrage-fly-mysql/etc/my.cnf:/etc/my.cnf
      - $PWD/ordinaryroad-barrage-fly-mysql/data:/var/lib/mysql
    environment:
      MYSQL_ROOT_PASSWORD:
    hostname: ordinaryroad-barrage-fly-mysql
    restart: always

  ordinaryroad-barrage-fly-ui:
    image: ordinaryroad-barrage-fly-ui
    container_name: ordinaryroad-barrage-fly-ui
    volumes:
      - $PWD/ordinaryroad-barrage-fly-ui/app:/ordinaryroad/ordinaryroad-barrage-fly-ui/app
    environment:
      BASE_URL: http://ordinaryroad-barrage-fly:8080
      RSA_PUBLIC_KEY:
      SUB_BASE_URL:
    hostname: ordinaryroad-barrage-fly-ui
    restart: always

  ordinaryroad-barrage-fly:
    image: ordinaryroad-barrage-fly
    container_name: ordinaryroad-barrage-fly
    environment:
      MYSQL_USERNAME: root
      MYSQL_PASSWORD: ${MYSQL_ROOT_PASSWORD}
      ADMIN_USERNAME:
      ADMIN_PASSWORD:
      RSA_PUBLIC_KEY:
      RSA_PRIVATE_KEY:
    hostname: ordinaryroad-barrage-fly
    restart: always

  ordinaryroad-barrage-fly-nginx:
    image: ordinaryroad-barrage-fly-nginx
    container_name: ordinaryroad-barrage-fly-nginx
    build:
      context: ./ordinaryroad-barrage-fly-nginx
    ports:
      - "81:81"
      - "8081:8081"
      - "9898:9898"
    hostname: ordinaryroad-barrage-fly-nginx
    restart: always
```

### 2.1.4 Example

```properties
# .env
# TODO 根据实际部署情况修改
MYSQL_HOST=192.168.1.2
MYSQL_PORT=3306
MYSQL_DATABASE=or_barrage_fly
MYSQL_USERNAME=root
MYSQL_PASSWORD=root
# TODO 根据实际部署情况修改
# 例如：增加一条host记录`127.0.0.1 ordinaryroad-barrage-fly`
# SUB_BASE_URL=ws://ordinaryroad-barrage-fly:9898
SUB_BASE_URL=ws://localhost:9898
RSA_PUBLIC_KEY=MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDNqVTCHbPojzNaR5TwhFxeKcuP/Po4J8WAc5dz1pHQ8FasH/hrSWwoFGpTTo6tfTl0mnAotu4p93kSMe+K2pc2VqUJwCcFj9cD6rhaKfjdj7/Kd2rHH43mXPI+OtggzzOKOOPsaMP5/r2Dyooafa1ChYDuSmf2fDM53CSIx+KDUwIDAQAB
ADMIN_USERNAME=admin
ADMIN_PASSWORD=admin
RSA_PRIVATE_KEY=MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAM2pVMIds+iPM1pHlPCEXF4py4/8+jgnxYBzl3PWkdDwVqwf+GtJbCgUalNOjq19OXSacCi27in3eRIx74ralzZWpQnAJwWP1wPquFop+N2Pv8p3ascfjeZc8j462CDPM4o44+xow/n+vYPKihp9rUKFgO5KZ/Z8MzncJIjH4oNTAgMBAAECgYEAs+ttoRzHJa8Rp+tzmy7Qd/hsg503ciUpHYUCfG68xmNcD90wQPvMyQuDMTpKi/A/cYkikhvnI4PCwW46N+mf2nJZEYG1DARTVj0lWaW+RqzerXG1Jg6v1WbgJwy5myZhFm9AOSb0OW3HskbRRyqxkiFX2Fr7ZKYXfrIWvooiVWECQQDnc0PPBpl5XPB+yGKepEatRwCMKdoIoD3R96Iykq9xowskafwob+gOn3mqEiH8YuDPOntcC4tqeLSzNiHYftKpAkEA43nN5SqvJZ3Gp/Vm4VY7DRAAT4cCxcxyYy3p1iuZf19y0UGdRrURQl4jx+I9RGSIy4v/hfpU05wnecvLh3KfmwJBANUz1pjUSXgEZv1C9aWRShHAP/7dZp1fjtLlvCG+AaM6P79RahiNzUP7H4XMokXth40dIBmQAOMZQct75/2YBdECQQCpwP0Y1pir/qkAME8dO+eHYPiKYJt+FosKXnoRXKoI9qbNaCTBXmBJ4czb3oaQImI/W/NM/ToOTIrdBmuVYcGfAkA6bkUO1fjjSC8N00xX1uGxJTcWwd9MQAffS+jm+C69lOyNW/azJjgXnmkbjKKB0kDcMDn6bkuYblcm4GOrVhqA
```

```yaml
# compose.yaml
version: "3.0"
services:
  ordinaryroad-barrage-fly-ui:
    image: ordinaryroad-barrage-fly-ui
    container_name: ordinaryroad-barrage-fly-ui
    environment:
      BASE_URL: http://ordinaryroad-barrage-fly:8080
      RSA_PUBLIC_KEY:
      SUB_BASE_URL:
    ports:
      - "30000:3000"
    hostname: ordinaryroad-barrage-fly-ui
    restart: always

  ordinaryroad-barrage-fly:
    image: ordinaryroad-barrage-fly-arm64
    container_name: ordinaryroad-barrage-fly
    environment:
      MYSQL_HOST:
      MYSQL_PORT:
      MYSQL_DATABASE:
      MYSQL_USERNAME:
      MYSQL_PASSWORD:
      ADMIN_USERNAME:
      ADMIN_PASSWORD:
      RSA_PUBLIC_KEY:
      RSA_PRIVATE_KEY:
    ports:
      - "8080:8080"
      - "9898:9898"
    hostname: ordinaryroad-barrage-fly
    restart: always
```

## 2.2 本地打包运行

1. clone项目到本地
2. 后端打包 `./gradlew clean bootJar`
3. 前端打包 `npm run build`