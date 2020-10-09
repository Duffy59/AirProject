FROM openjdk:11
COPY ./target/AirProject-1.0.jar /usr/app/
WORKDIR /usr/app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "AirProject-1.0.jar"]