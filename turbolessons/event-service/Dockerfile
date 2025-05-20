#
# Build stage
#
FROM maven:3.8-openjdk-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

#
# Package stage
#
FROM openjdk:17
COPY --from=build /home/app/target/event-service-0.0.1-SNAPSHOT.jar /usr/local/lib/event-service.jar
EXPOSE 5001

# Set default profile to default if not specified
ENV SPRING_PROFILES_ACTIVE=default

ENTRYPOINT ["java", \
            "-Djdk.internal.platform.useContainer=false", \
            "-jar", "/usr/local/lib/event-service.jar", \
            "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]