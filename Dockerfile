FROM openjdk:11-jre-slim
COPY target/awesome-payment-service-0.0.1-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["java","-jar","/app.jar"]
