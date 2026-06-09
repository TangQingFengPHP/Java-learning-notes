# syntax=docker/dockerfile:1

FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY docker/maven-settings.xml /root/.m2/settings.xml
COPY pom.xml pom.xml

RUN --mount=type=cache,target=/root/.m2/repository \
    mvn -B -ntp -DskipTests dependency:go-offline

COPY src src/

RUN --mount=type=cache,target=/root/.m2/repository \
    mvn -B -ntp -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/target/tomcat-practice.war /app/app.war
ENV SERVER_PORT=8187
EXPOSE 8187
ENTRYPOINT ["java","-jar","/app/app.war"]
