# syntax=docker/dockerfile
FROM maven:3.6.3-jdk-11 as build
WORKDIR /workspace/app
COPY pom.xml .
COPY src src
RUN --mount=type=cache,target=/root/.m2,mode=0777 mvn clean package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:11-jre-slim-buster

LABEL maintainer="noisonnoiton"

# COPY --from=build /workspace/app/target/awesome-payment-service-0.0.1-SNAPSHOT.jar app.jar
# ENV JAVA_OPTS=""
# ENTRYPOINT ["java","-jar","/app.jar"]

RUN mkdir -p /app/bin

COPY --from=build /workspace/app/target/awesome-payment-service-0.0.1-SNAPSHOT.jar /app/bin/app.jar
COPY opentelemetry-javaagent-all.jar /app/bin

ENV JAVA_OPTS="-Dotel.exporter.jaeger.service.name=payment.shop -Dotel.exporter=jaeger -Dotel.exporter.jaeger.endpoint=localhost:14250 -Dapplication.home=/app/bin/"
CMD java $JAVA_OPTS -javaagent:/app/bin/opentelemetry-javaagent-all.jar \
		 -jar \
		 /app/bin/app.jar
