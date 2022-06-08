FROM openjdk:18-alpine

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
RUN mkdir /storage

ENTRYPOINT ["java","-jar","/app.jar"]