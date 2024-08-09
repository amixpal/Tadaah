# Stage 1: Build the JAR file
FROM openjdk:17-jdk-slim as build

# Set the working directory
WORKDIR /app

# Copy the Gradle wrapper and settings into the working directory
COPY gradlew /app/gradlew
COPY gradle /app/gradle

# Copy the specific module's build file and source code
COPY notification-service/build.gradle.kts /app/notification-service/build.gradle.kts
COPY notification-service/src /app/notification-service/src
COPY settings.gradle.kts /app/settings.gradle.kts

# Grant execute permissions to gradlew
RUN chmod +x /app/gradlew

# Build the JAR file
RUN /app/gradlew :notification-service:build --parallel --stacktrace

# Stage 2: Create the final image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/notification-service/build/libs/notification-service-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8081
# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
