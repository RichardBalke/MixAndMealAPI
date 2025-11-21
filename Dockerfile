# Stage 1: Build the application
FROM gradle:jdk24-alpine AS build
WORKDIR /app
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
COPY src ./src
RUN gradle buildFatJar --no-daemon

# Stage 2: Create the runtime image
FROM eclipse-temurin:24-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar ./ktor-app.jar
EXPOSE 8080
CMD ["java", "-jar", "ktor-app.jar"]
