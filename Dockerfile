FROM gradle:8.7.0-jdk21-alpine AS build
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .
RUN gradle bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
ARG JAR_FILE=/home/gradle/project/build/libs/*.jar
COPY --from=build ${JAR_FILE} app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=docker"]
