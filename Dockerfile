FROM openjdk:11-jdk
EXPOSE 8090
ADD target/diplom-0.0.1-SNAPSHOT.jar j.jar
ENTRYPOINT ["java", "-jar", "/j.jar"]