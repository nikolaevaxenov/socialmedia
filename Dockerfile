FROM eclipse-temurin:17-jdk-alpine

COPY target/app-0.0.1-SNAPSHOT.jar app.jar
RUN mkdir "savedImages"

ENTRYPOINT ["java","-jar","/app.jar"]