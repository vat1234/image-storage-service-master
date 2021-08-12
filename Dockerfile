FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/image-storage-service-0.1.0.jar image-storage-service.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/image-storage-service.jar"]
