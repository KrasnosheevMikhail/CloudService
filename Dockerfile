FROM openjdk:17-alpine
EXPOSE 8888
ADD target/CloudService-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]