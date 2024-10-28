FROM azul/zulu-openjdk-alpine:17
RUN rm -rf payment
EXPOSE 8884
ADD application/target/payment-api-0.0.1.jar app.jar
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]