FROM mysql/mysql-server:8.0.26

LABEL maintainer="1962247851@qq.com"

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY ./sql /docker-entrypoint-initdb.d

COPY ./etc/my.cnf /etc/my.cnf