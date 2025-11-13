FROM openjdk:21-jdk-slim
#donde esta el ejecutable
ARG JAR_FILE=target/servidor-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app_servidor.jar
 #puerto por el que voy a acceder al docker
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "app_servidor.jar"]