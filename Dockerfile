# ---- Base Maven build ----
FROM maven:3.5-jdk-8-alpine as build
WORKDIR /app

# Cache dependencies as long as the POM changes
COPY ./pom.xml ./kommonitor-management/pom.xml
RUN mvn -f ./kommonitor-management/pom.xml dependency:go-offline --fail-never

# Copy source files for build
COPY . /app/kommonitor-management/

# Run the Maven build
RUN mvn -f ./kommonitor-management/pom.xml clean install -DskipTests

# ---- Run the application ----
FROM openjdk:alpine
WORKDIR /app

# Copy from the base build image
COPY --from=build app/kommonitor-management/target/kommonitor-data-management-api-1.0.0.jar /app/kommonitor-management-app.jar

# Set the entrypoint for starting the app
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=docker","-jar","/app/kommonitor-management-app.jar"]