# Use Amazon Corretto JDK 17 as the base image
FROM openjdk:17-jdk-slim

# Copy the JAR file from the build directory to the image
COPY build/libs/PrePay-0.0.1-SNAPSHOT.jar spring-base.jar
COPY src/main/resources/firebase-service-key.json ./src/main/resources/
COPY src/main/resources/fcm-google-services.json ./src/main/resources/
ENV SPRING_PROFILES_ACTIVE=prod
# Define the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "/spring-base.jar"]



