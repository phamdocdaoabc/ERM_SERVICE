FROM openjdk:21

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} erm-service.jar

ENTRYPOINT ["java", "-jar", "erm-service.jar"]

EXPOSE 8080