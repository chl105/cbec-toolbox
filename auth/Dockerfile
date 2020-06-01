FROM adoptopenjdk:11-openj9

ARG JAR_FILE_NAME
ADD target/$JAR_FILE_NAME /

ENTRYPOINT java -Dspring.profiles.active=prod $PARAMS -jar /*.jar