FROM azul/zulu-openjdk-alpine:17
RUN rm -rf report
EXPOSE 8883
ADD application/target/payment-report-0.0.1.jar app.jar
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]