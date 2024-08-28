FROM azul/zulu-openjdk-alpine:17
RUN rm -rf userapi
EXPOSE 8085
ADD application/target/application-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]