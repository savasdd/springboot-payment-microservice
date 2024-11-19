FROM azul/zulu-openjdk-alpine:17
RUN rm -rf user
EXPOSE 8880
ADD application/target/payment-user-0.0.1.jar app.jar
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]