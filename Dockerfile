FROM openjdk:8-jdk-alpine
MAINTAINER Christian Danowski-Buhren <christian.danowski-buhren@hs-bochum.de>
ADD ./target/kommonitor-data-management-api-1.0.0.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=docker","-jar","/app.jar"]