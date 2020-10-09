FROM maven:3.6.3-jdk-11
COPY . .
RUN mvn clean package
WORKDIR /target/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "AirProject-1.0.jar"]