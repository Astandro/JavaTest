# Use official OpenJDK base image from Docker Hub
FROM openjdk:17-jdk-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the local project files into the container
COPY . /app

# Build the project using Maven (assuming you have a pom.xml)
RUN ./mvnw clean package -DskipTests

# Use OpenJDK runtime image for the final stage
FROM openjdk:17-jre-slim

# Set the working directory in the final container
WORKDIR /app

# Copy the built jar file from the build stage
COPY --from=build /app/target/your-app-name.jar /app/your-app-name.jar

# Expose the port your app will run on (default Spring Boot port)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/your-app-name.jar"]
