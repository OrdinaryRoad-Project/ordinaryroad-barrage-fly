FROM mysql/mysql-server:8.0.26

MAINTAINER mjz(or-mjz@qq.com)

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY ./sql /docker-entrypoint-initdb.d

COPY ./etc/my.cnf /etc/my.cnf