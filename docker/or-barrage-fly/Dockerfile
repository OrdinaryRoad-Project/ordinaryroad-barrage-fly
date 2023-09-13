FROM btgoose/jdk17

MAINTAINER mjz(or-mjz@qq.com)

ARG APP_NAME=or-barrage-fly

RUN mkdir -p /ordinaryroad/$APP_NAME/app

WORKDIR /ordinaryroad/$APP_NAME/app

COPY ./app/$APP_NAME.jar app.jar

EXPOSE 8080 9090/udp

# 防止日志中文乱码
ENV LANG=C.UTF-8

ENV TZ=Asia/Shanghai JAVA_OPTS=""

CMD sleep 3; java -jar $JAVA_OPTS app.jar
