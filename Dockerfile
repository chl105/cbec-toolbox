FROM maven:3-openjdk-11 as builder

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories

ADD . .

RUN mvn package
