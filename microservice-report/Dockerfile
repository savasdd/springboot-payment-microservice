
FROM openjdk:17-oracle
ADD application/target/payment-report-0.0.1.jar app.jar
EXPOSE 8883
ENTRYPOINT ["java","-jar","app.jar"]