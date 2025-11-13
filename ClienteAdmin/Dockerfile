FROM openjdk:21-jdk-slim
#donde esta el ejecutable 
ARG JAR_FILE=target/clienteAdmin-0.0.1-SNAPSHOT.jar 
COPY ${JAR_FILE} app_ClienteAdmin.jar
 #puerto por el que voy a acceder al docker
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app_ClienteAdmin.jar"]