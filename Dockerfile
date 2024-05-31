# Use the official Maven image to build the Spring Boot application
FROM maven:3.9.7-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the pom.xml file and download dependencies only
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the entire project and build the application
COPY src ./src
RUN mvn clean package

# Use the official OpenJDK image to run the application
FROM openjdk:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]