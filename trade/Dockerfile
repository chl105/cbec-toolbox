FROM registry.cn-hangzhou.aliyuncs.com/gosun/alpine-java:jre11

ARG JAR_FILE_NAME
ADD target/$JAR_FILE_NAME /

ENTRYPOINT java -Dspring.profiles.active=prod $PARAMS -jar /*.jar