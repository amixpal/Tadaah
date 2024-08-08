# Stage 1: Build the JAR file
FROM openjdk:17-jdk-alpine as build

# Set the working directory
WORKDIR /app

# Copy the Gradle wrapper and settings into the working directory
COPY gradlew /app/gradlew
COPY gradle /app/gradle

# Copy the specific module's build file and source code
COPY document-service/build.gradle.kts /app/document-service/build.gradle.kts
COPY document-service/src /app/document-service/src
COPY settings.gradle.kts /app/settings.gradle.kts

# Grant execute permissions to gradlew
RUN chmod +x /app/gradlew

# Build the JAR file
RUN /app/gradlew :document-service:build --parallel --stacktrace

# Stage 2: Create the final image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/document-service/build/libs/document-service-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080
# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
