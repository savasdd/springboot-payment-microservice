FROM azul/zulu-openjdk-alpine:17
RUN rm -rf discovery
EXPOSE 8761
ADD application/target/discovery-0.0.1.jar app.jar
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]