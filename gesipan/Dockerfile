FROM openjdk:11-jdk
COPY ./gesipan/build/libs/*.jar ./gesipan.jar
EXPOSE 80
ENTRYPOINT ["java", "-jar", "./gesipan.jar"]
