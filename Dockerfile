FROM maven:3.8.5-openjdk-17 AS build
LABEL authors="rioswarawan"
WORKDIR /app

COPY . .
RUN mvn dependency:go-offline -B
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

RUN echo "java $JVM_ARGS -XX:+UseZGC -XshowSettings:vm -Xlog:gc -jar app.jar" > /entrypoint.sh
RUN chmod a+x /entrypoint.sh

EXPOSE 8080
ENTRYPOINT ["/bin/sh", "/entrypoint.sh"]