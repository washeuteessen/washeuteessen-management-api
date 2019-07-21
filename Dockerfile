FROM openjdk:8-jdk-alpine
COPY target/washeuteessen-management-api-1.0-SNAPSHOT.jar app.jar
VOLUME /sitemap
EXPOSE 8080
ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap" ,"-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
