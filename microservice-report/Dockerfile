FROM azul/zulu-openjdk-alpine:17
RUN rm -rf report
EXPOSE 8088
ADD application/target/stock-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]