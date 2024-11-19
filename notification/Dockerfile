FROM azul/zulu-openjdk-alpine:17
RUN rm -rf notification
EXPOSE 8882
ADD application/target/payment-notification-0.0.1.jar app.jar
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]