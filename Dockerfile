FROM openjdk:8-jdk-alpine
MAINTAINER Christian Danowski-Buhren <christian.danowski-buhren@hs-bochum.de>

VOLUME /tmp
ARG JAR_FILE
ADD ./target/kommonitor-data-management-api-1.0.0.jar app.jar

ENV JAVA_OPTS=""

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=docker","-jar","/app.jar"]
