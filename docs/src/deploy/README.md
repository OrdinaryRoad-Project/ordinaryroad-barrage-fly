# 2 项目部署

项目前后端分离，可以使用Docker Compose进行部署，或者clone项目到本地后分别打包后部署
::: info
如需协助部署，请前往频道联系频道主
:::

- 演示DEMO：[https://barragefly.demo.ordinaryroad.tech](https://barragefly.demo.ordinaryroad.tech)
- WebSocket地址：wss://barragefly-rsocket.demo.ordinaryroad.tech

> 帐号请前往[QQ频道](https://pd.qq.com/s/3id0n7fvs)获取
> <img src="/assets/image/QQPD.jpg" width="400"/><img src="/assets/image/WeChatPD.jpg" width="400"/>

## 2.1 Docker Compose 部署

### 2.1.0 Tips

- Docker Compose示例位于`.docker`目录中
    - `compose.yaml`: 前端+后端，需要手动初始化数据库
    - `compose-with-mysql.yaml`: 前端+后端+MySQL
    - `compose-with-nginx.yaml`: 前端+后端+Nginx，需要手动初始化数据库
    - `compose-with-mysql-nginx.yaml`: 前端+后端+MySQL+Nginx
- 数据库初始化SQL文件位于`.docker/ordinaryroad-barrage-fly-mysql/sql/or_barrage_fly.sql`
- 使用MySQL或Nginx时需要先build
    - `docker compose -f compose.yaml build ordinaryroad-barrage-fly-mysql`
    - `docker compose -f compose.yaml build ordinaryroad-barrage-fly-nginx`

镜像加速与拉取：[仓库链接](https://ordinaryroad.coding.net/public-artifacts/ordinaryroad-barrage-fly/docker-pub/packages)

### 2.1.1 Example

下面是一个部署前端+后端的示例，请确保以下端口号未被占用，或者修改为其他端口

- 后端：8080，9898
- 前端：30000

1 新建一个文件夹，文件夹内只需要一个`.env`文件和一个`compose.yaml`文件，文件内容在步骤后面

2 拉取镜像

```bash
# DOCKER_OPTS="--registry-mirror=https://mirror.ccs.tencentyun.com"
# 拉取最新版1.6.2
docker pull ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly:1.6.2
docker pull ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-ui:1.6.2 
```

3 给镜像重新打TAG

```bash
docker tag ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly:1.6.2 ordinaryroad-barrage-fly
docker tag ordinaryroad-docker.pkg.coding.net/ordinaryroad-barrage-fly/docker-pub/ordinaryroad-barrage-fly-ui:1.6.2 ordinaryroad-barrage-fly-ui
```

4 进入`compose.yaml`文件所在的文件夹，执行启动容器命令`docker compose up -d`

5 浏览器访问 http://localhost:30000

#### .env

```properties
# .env
# TODO 根据实际部署情况修改
# 注意：请勿用localhost、127.0.0.1，要求能够被容器访问到的地址
MYSQL_HOST=192.168.1.2
MYSQL_PORT=3306
MYSQL_DATABASE=or_barrage_fly
MYSQL_USERNAME=root
MYSQL_PASSWORD=root
# TODO 根据实际部署情况修改
# 主机名要求能够被浏览器等客户端访问到，端口为容器映射到主机的端口，默认9898
SUB_BASE_URL=ws://localhost:9898
# TODO 管理后台的账号密码
ADMIN_USERNAME=admin
ADMIN_PASSWORD=admin
# TODO 根据实际需要修改
JAVA_OPTS="-Xmn256m"
# 主机名要求能够被浏览器等客户端访问到，端口号为容器映射到主机的端口，默认8080
SPRING_BOOT_ADMIN_BASE_URL=http://localhost:8080/admin
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

1. 克隆项目到本地 `git clone https://github.com/OrdinaryRoad-Project/ordinaryroad-barrage-fly.git`
2. 后端打包 `./gradlew clean bootJar`
3. 前端打包 `npm install && npm run build`