FROM nginx:1.21.3

MAINTAINER mjz(or-mjz@qq.com)

VOLUME $PWD/conf.d /etc/nginx/conf.d
VOLUME $PWD/ssl /etc/nginx/ssl

ARG APP_NAME=ordinaryroad-barrage-fly-docs

RUN mkdir -p /ordinaryroad/$APP_NAME/app

WORKDIR /ordinaryroad/$APP_NAME/app

COPY ./app .

EXPOSE 80 443