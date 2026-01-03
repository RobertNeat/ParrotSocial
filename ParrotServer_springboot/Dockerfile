# Use Eclipse Temurin JDK 25 for building and running
FROM eclipse-temurin:25-jdk

# Set working directory
WORKDIR /app

# Copy the JAR file (you'll need to build your Spring Boot app first)
# Place your built JAR file in the ProjectBackend directory as app.jar
#COPY app.jar /app/app.jar
COPY /target/ParrotServer-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]