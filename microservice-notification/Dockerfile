FROM azul/zulu-openjdk-alpine:17
RUN rm -rf notification
EXPOSE 8088
ADD application/target/notification-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]