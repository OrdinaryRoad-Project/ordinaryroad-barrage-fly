FROM nginx:1.21.3

MAINTAINER mjz(or-mjz@qq.com)

ARG APP_NAME=ordinaryroad-barrage-fly-docs

RUN mkdir -p /ordinaryroad/$APP_NAME/app

WORKDIR /ordinaryroad/$APP_NAME/app

COPY ./app .

EXPOSE 80 443