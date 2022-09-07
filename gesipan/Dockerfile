FROM openjdk:11-jdk
ARG JAR_FILE=./gesipan/build/libs/*T.jar
COPY ${JAR_FILE} gesipan.jar
ENTRYPOINT ["java", "-jar", "/gesipan.jar"]
