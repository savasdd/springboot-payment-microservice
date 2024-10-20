FROM azul/zulu-openjdk-alpine:17
RUN rm -rf gateway
EXPOSE 8085
ADD application/target/gateway-0.0.1.jar app.jar
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]