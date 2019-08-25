FROM openjdk:8-jdk-alpine
ADD target/UserAuthentication-Service-0.0.1-SNAPSHOT.jar app.jar
RUN chmod 777 app.jar
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar