FROM azul/zulu-openjdk-alpine:17
RUN rm -rf stock
EXPOSE 8881
ADD application/target/payment-stock-0.0.1.jar app.jar
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]