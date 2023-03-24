# Start with a base image containing Java runtime
FROM eclipse-temurin:17-jre-alpine

# The application's jar file
ARG JAR_FILE=build/libs/*.jar

# Add the application's jar to the container
COPY ${JAR_FILE} app.jar

# Set up environment variables
ENV MINIO_ENDPOINT minio:9000
ENV MINIO_ACCESS_KEY minioaccesskey
ENV MINIO_SECRET_KEY miniosecretkey

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app.jar"]
