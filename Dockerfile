FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} demo.jar
ENTRYPOINT ["java","-jar","/demo.jar"]