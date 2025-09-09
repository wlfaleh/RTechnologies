FROM maven:3.9.3-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/RTechnologies-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 6060
CMD ["java", "-jar", "app.jar"]
