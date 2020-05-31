FROM alpine:latest as builder

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories  && \
    apk add nodejs yarn && \
    yarn config set registry 'https://registry.npm.taobao.org'

WORKDIR /root/web

ADD  . .
ADD default.conf /etc/nginx/conf.d/

RUN yarn install && yarn build

FROM nginx:alpine

COPY --from=builder /root/web/dist/ /usr/share/nginx/html/

ENV TZ Asia/ShangHai