FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml /workspace/pom.xml
COPY src /workspace/src
COPY mybatis-flex.config /workspace/mybatis-flex.config
RUN mvn -q -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/target/*.jar /app/app.jar
ENV SERVER_PORT=8180
EXPOSE 8180
ENTRYPOINT ["java","-jar","/app/app.jar"]

