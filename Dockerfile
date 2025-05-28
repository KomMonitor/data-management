# ---- Base Maven build ----
FROM maven:3-eclipse-temurin-17-alpine as build
WORKDIR /app

# Cache dependencies as long as the POM changes
COPY ./pom.xml ./kommonitor-management/pom.xml
RUN mvn -f ./kommonitor-management/pom.xml dependency:go-offline --fail-never

# Copy source files for build
COPY . /app/kommonitor-management/

# Run the Maven build
RUN mvn -f ./kommonitor-management/pom.xml clean install -Dapp.finalName=kommonitor-data-management-app -DskipTests

# ---- Run the application ----
FROM eclipse-temurin:17.0.11_9-jdk-alpine
WORKDIR /app

# Copy from the base build image
COPY --from=build app/kommonitor-management/target/kommonitor-data-management-app.jar /app/kommonitor-data-management-app.jar

# Set the command for starting the app
CMD ["sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=docker -jar /app/kommonitor-data-management-app.jar"]