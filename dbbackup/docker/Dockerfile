FROM alpine:latest

ADD docker-entrypoint-initdb.d /docker-entrypoint-initdb.d/
ADD cron.daily /etc/periodic/daily/
ADD entrypoint.sh /
ADD dbbackup-*.jar /app.jar

RUN  sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories && \
     apk add --update mysql-client tzdata bash && \
     rm -rf /var/cache/apk/* && \
     sed -i 's/[\r]*$//g' /etc/periodic/daily/db-backup && \
     chmod 777  /etc/periodic/daily/db-backup  && \
     sed -i 's/[\r]*$//g' /entrypoint.sh && \
     chmod 777 /entrypoint.sh && \
     ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

ENTRYPOINT ["/entrypoint.sh"]

