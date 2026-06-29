# syntax=docker/dockerfile:1

FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY docker/maven-settings.xml /root/.m2/settings.xml

COPY pom.xml pom.xml
COPY maven-common/pom.xml maven-common/pom.xml
COPY maven-domain/pom.xml maven-domain/pom.xml
COPY maven-service/pom.xml maven-service/pom.xml
COPY maven-web/pom.xml maven-web/pom.xml

RUN --mount=type=cache,target=/root/.m2/repository \
    mvn -B -ntp -DskipTests dependency:go-offline -pl maven-web -am

COPY maven-common maven-common
COPY maven-domain maven-domain
COPY maven-service maven-service
COPY maven-web maven-web

RUN --mount=type=cache,target=/root/.m2/repository \
    mvn -B -ntp -DskipTests package -pl maven-web -am

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/maven-web/target/*.jar /app/app.jar
ENV SERVER_PORT=8193
EXPOSE 8193
ENTRYPOINT ["java","-jar","/app/app.jar"]
