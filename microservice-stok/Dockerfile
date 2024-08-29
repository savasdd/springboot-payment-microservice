FROM azul/zulu-openjdk-alpine:17
RUN rm -rf stock
EXPOSE 8086
ADD application/target/stock-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]