# 2 项目部署

项目前后端分离，可以使用Docker Compose进行部署，或者clone项目到本地后分别打包后部署

- 演示DEMO：[https://barragefly.ordinaryroad.tech:7000](https://barragefly.ordinaryroad.tech:7000)
- WebSocket地址：wss://barragefly.ordinaryroad.tech:7100

> 帐号请前往[QQ频道](https://pd.qq.com/s/3id0n7fvs)获取

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
> # 拉取最新版1.3.0
> docker pull ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly:1.3.0
> docker pull ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-arm64:1.3.0
> docker pull ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-ui:1.3.0
> ```

> 拉到本地后需要根据compose文件重命名镜像
> ```shell
> docker tag ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly:1.3.0 ordinaryroad-barrage-fly
> docker tag ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-arm64:1.3.0 ordinaryroad-barrage-fly-arm64
> docker tag ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-ui:1.3.0 ordinaryroad-barrage-fly-ui
> ```

### 2.1.1 前端+后端

```yaml
# 需要的环境变量，其他可以环境变量可看每个目录下的.env文件，初始化sql位于`.docker/ordinaryroad-barrage-fly-mysql/sql/or_barrage_fly.sql`
# MYSQL_HOST（注意请勿用localhost、127.0.0.1）
# MYSQL_PORT
# MYSQL_DATABASE
# MYSQL_USERNAME
# MYSQL_PASSWORD
# ADMIN_USERNAME（任务管理后台登录用户名，默认admin）
# ADMIN_PASSWORD（任务管理后台登录密码，默认admin）
# SPRING_BOOT_ADMIN_BASE_URL（根据后端部署情况设置Spring Boot Admin的地址）
# SUB_BASE_URL（根据后端部署情况设置Client要连接的WebSocket地址）

version: "3.0"
services:
  ordinaryroad-barrage-fly-ui:
    image: ordinaryroad-barrage-fly-ui
    container_name: ordinaryroad-barrage-fly-ui
    environment:
      BASE_URL: http://ordinaryroad-barrage-fly:8080
      SPRING_BOOT_ADMIN_BASE_URL: http://localhost:8080/admin
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
      JAVA_OPTS:
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
# ADMIN_USERNAME（任务管理后台登录用户名，默认admin）
# ADMIN_PASSWORD（任务管理后台登录密码，默认admin）
# SPRING_BOOT_ADMIN_BASE_URL（根据后端部署情况设置Spring Boot Admin的地址）
# SUB_BASE_URL（根据后端部署情况设置Client要连接的WebSocket地址）

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
    environment:
      BASE_URL: http://ordinaryroad-barrage-fly:8080
      SPRING_BOOT_ADMIN_BASE_URL: http://localhost:8080/admin
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
      JAVA_OPTS:
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
# ADMIN_USERNAME（任务管理后台登录用户名，默认admin）
# ADMIN_PASSWORD（任务管理后台登录密码，默认admin）
# SPRING_BOOT_ADMIN_BASE_URL（根据后端部署情况设置Spring Boot Admin的地址）
# SUB_BASE_URL（根据后端部署情况设置Client要连接的WebSocket地址）

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
    environment:
      BASE_URL: http://ordinaryroad-barrage-fly:8080
      SPRING_BOOT_ADMIN_BASE_URL: http://localhost:8080/admin
      SUB_BASE_URL:
    hostname: ordinaryroad-barrage-fly-ui
    restart: always

  ordinaryroad-barrage-fly:
    image: ordinaryroad-barrage-fly
    container_name: ordinaryroad-barrage-fly
    environment:
      MYSQL_USERNAME: root
      MYSQL_PASSWORD: ${MYSQL_ROOT_PASSWORD}
      ADMIN_BASE_URL:
      ADMIN_USERNAME:
      ADMIN_PASSWORD:
    hostname: ordinaryroad-barrage-fly
    restart: always

  ordinaryroad-barrage-fly-nginx:
    image: ordinaryroad-barrage-fly-nginx
    container_name: ordinaryroad-barrage-fly-nginx
    build:
      context: ./ordinaryroad-barrage-fly-nginx
    ports:
      # UI
      - "81:81"
      # Admin
      - "8080:8080"
      # 后端
      - "8081:8081"
      # RSocket
      - "9898:9898"
    hostname: ordinaryroad-barrage-fly-nginx
    depends_on:
      - ordinaryroad-barrage-fly
      - ordinaryroad-barrage-fly-ui
    restart: always
```

### 2.1.4 Example

新建一个文件夹，文件夹内只需要一个`.env`文件和一个`compose.yaml`文件

#### .env

```properties
# .env
# TODO 根据实际部署情况修改
MYSQL_HOST=192.168.1.2
MYSQL_PORT=3306
MYSQL_DATABASE=or_barrage_fly
MYSQL_USERNAME=root
MYSQL_PASSWORD=root
# TODO 根据实际部署情况修改
SUB_BASE_URL=ws://localhost:9898
# TODO 管理后台的账号密码
ADMIN_USERNAME=admin
ADMIN_PASSWORD=admin
# TODO 根据实际需要修改
JAVA_OPTS="-Xmn256m"
```

#### compose.yaml

```yaml
# compose.yaml
version: "3.0"
services:
  ordinaryroad-barrage-fly-ui:
    image: ordinaryroad-barrage-fly-ui
    container_name: ordinaryroad-barrage-fly-ui
    environment:
      BASE_URL: http://ordinaryroad-barrage-fly:8080
      SPRING_BOOT_ADMIN_BASE_URL: http://localhost:8080/admin
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
      JAVA_OPTS:
    ports:
      # 用于访问Spring Boot Admin
      - "8080:8080"
      # 用于连接RSocket
      - "9898:9898"
    hostname: ordinaryroad-barrage-fly
    restart: always
```

## 2.2 本地打包运行

1. clone项目到本地
2. 后端打包 `./gradlew clean bootJar`
3. 前端打包 `npm run build`