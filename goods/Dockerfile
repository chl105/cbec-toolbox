FROM registry.cn-hangzhou.aliyuncs.com/gosun/openjdk-11-openj9:1.0.0

ARG JAR_FILE_NAME
ADD target/$JAR_FILE_NAME /

ENTRYPOINT java -Dspring.profiles.active=prod $PARAMS -jar /*.jar