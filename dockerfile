FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/RTechnologies-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 6060
CMD ["java", "-jar", "app.jar"]