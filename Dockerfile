# syntax=docker/dockerfile:1
ARG MAVEN_VERSION=3.9.7-eclipse-temurin-21-alpine
ARG ECLIPSE_TEMURIN_VERSION=21.0.3_9-jre-alpine

#build image
FROM maven:$MAVEN_VERSION as build
WORKDIR /app/server
COPY  . .
RUN mvn package -DskipTests

#run image
FROM eclipse-temurin:$ECLIPSE_TEMURIN_VERSION as RUN
COPY --from=build ["/app/server/target/*-exec.jar", "/app/server/shopify-exec.jar"]
WORKDIR /app/server

EXPOSE 8080
CMD [ "java","--add-exports", "jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED", "-jar","shopify-exec.jar"]






