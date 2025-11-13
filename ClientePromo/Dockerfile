FROM openjdk:21-jdk-slim
#donde esta el ejecutable
ARG JAR_FILE=target/clientePromo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app_ClientePromo.jar
#puerto por el que voy a acceder al docker
EXPOSE 8050
ENTRYPOINT ["java", "-jar", "app_ClientePromo.jar"]