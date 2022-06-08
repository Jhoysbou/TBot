FROM openjdk:18-alpine

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
RUN mkdir /storage
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]
