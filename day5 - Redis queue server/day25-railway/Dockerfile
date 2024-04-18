# Starting with this Linux server
FROM maven:3-eclipse-temurin-21 AS sb-builder

## Build the application
# Create a directory call /sbapp
# go into the directory cd /app
WORKDIR /sbapp

# everything after this is in /sbapp
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY .mvn .mvn
COPY src src

# Build the application
RUN mvn package -Dmaven.test.skip=true

FROM openjdk:21-jdk-bullseye

WORKDIR /app 

COPY --from=sb-builder /sbapp/target/day25-0.0.1-SNAPSHOT.jar app.jar

## Run the application
# Define environment variable 
ENV PORT=8080 
ENV SPRING_REDIS_HOST=localhost SPRING_REDIS_PORT=6379 SPRING_REDIS_DATABASE=0 SPRING_REDIS_USER= SPRING_REDIS_PASSWORD=

# Expose the port
EXPOSE ${PORT}

# Run the program
ENTRYPOINT SERVER_PORT=${PORT} java -jar app.jar
