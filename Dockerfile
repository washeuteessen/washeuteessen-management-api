FROM maven:3-jdk-8
COPY . /data
WORKDIR /data
RUN mvn clean package

FROM openjdk:8-jdk-alpine
VOLUME /index
COPY --from=0 /data/target/washeuteessen-management-api-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap" ,"-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
