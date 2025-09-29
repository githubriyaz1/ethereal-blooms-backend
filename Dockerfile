# Start with a base image that has Maven and Java 21 already installed
FROM maven:3.9-eclipse-temurin-21-alpine

# Set the working directory inside our little computer
WORKDIR /app

# Copy the whole project into the computer
COPY . .

# Run the Maven build command to create the executable .jar file
# This is the same command that slayed the final boss before
RUN mvn clean install -DskipTests

# Tell the computer that the app will be listening on port 8080
EXPOSE 8080

# This is the final command to run when the computer starts up
ENTRYPOINT ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]