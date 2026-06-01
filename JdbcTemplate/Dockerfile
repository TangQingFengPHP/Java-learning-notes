# syntax=docker/dockerfile:1

FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY docker/maven-settings.xml /root/.m2/settings.xml
COPY pom.xml pom.xml

# 仅 pom 变化时重新下载依赖；.m2 通过 BuildKit 缓存复用
RUN --mount=type=cache,target=/root/.m2/repository \
    mvn -B -ntp -DskipTests dependency:go-offline

COPY src src/

RUN --mount=type=cache,target=/root/.m2/repository \
    mvn -B -ntp -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/target/*.jar /app/app.jar
ENV SERVER_PORT=8181
EXPOSE 8181
ENTRYPOINT ["java","-jar","/app/app.jar"]
