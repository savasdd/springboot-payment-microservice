FROM azul/zulu-openjdk-alpine:17
RUN rm -rf payment
EXPOSE 8087
ADD application/target/payment-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]