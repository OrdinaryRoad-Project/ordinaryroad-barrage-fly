FROM node:16.18.0

MAINTAINER mjz(or-mjz@qq.com)

ARG APP_NAME=ordinaryroad-barrage-fly-ui

RUN mkdir -p /ordinaryroad/$APP_NAME/app

WORKDIR /ordinaryroad/$APP_NAME/app

COPY ./app .

EXPOSE 3000

ENV HOST=0.0.0.0

CMD npm run start