FROM openjdk:11-jdk
ARG JAR_FILE=./gesipan/build/libs/*T.jar
COPY ${JAR_FILE} gesipan.jar
EXPOSE 80
ENTRYPOINT ["java", "-jar", "/gesipan.jar"]
