#FROM openjdk:8-jre-alpine
FROM adoptopenjdk/openjdk11:jre-11.0.8_10-alpine
EXPOSE 8080

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
#ARG JAR_FILE=target/my-application.jar
#ADD ${JAR_FILE} app.jar

RUN addgroup -S app && adduser -S app -G app
USER app

ENTRYPOINT ["java","-jar","/app.jar"]

############## TEMP NOTES FOR FUTURE DEVELOPMENTS #####################
# PROD
#FROM ubuntu-jdk
#
#MAINTAINER 51178702+P1100@users.noreply.github.com
#
#ENV version=aws-db-usage
#
#ENV dbuser=postgres
#ENV dbpass=password
#ENV jdbcurl=jdbc:
#
#WORKDIR /usr/local/bin
#
#ADD target/a-app.jar .
#
#ENTRYPOINT ["java", "-jar", "pma-app.jar"]
