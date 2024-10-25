FROM azul/zulu-openjdk-alpine:17
RUN rm -rf user
EXPOSE 8880
ADD application/target/user-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]