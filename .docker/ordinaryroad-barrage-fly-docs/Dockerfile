FROM nginx:1.21.3

LABEL maintainer="1962247851@qq.com"

ARG APP_NAME=ordinaryroad-barrage-fly-docs

RUN mkdir -p /ordinaryroad/$APP_NAME/app

WORKDIR /ordinaryroad/$APP_NAME/app

COPY ./app .

COPY ./conf.d /etc/nginx/conf.d

EXPOSE 80