# Use Amazon Corretto JDK 17 as the base image
FROM amazoncorretto:21

# Copy the JAR file from the build directory to the image
COPY build/libs/PrePay-0.0.1-SNAPSHOT.jar spring-base.jar

ENV SPRING_PROFILES_ACTIVE=prod
# Define the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "/spring-base.jar"]



