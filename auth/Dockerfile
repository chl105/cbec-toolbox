FROM  registry.cn-hangzhou.aliyuncs.com/lzk90s/openjdk11-openj9:alpine-jre

ARG JAR_FILE_NAME
ADD target/$JAR_FILE_NAME /

ENTRYPOINT java -Dspring.profiles.active=prod $PARAMS -jar /*.jar