FROM maven:3.9-eclipse-temurin-17 AS build
ARG MODULE
WORKDIR /app
COPY . .
RUN mvn -q -pl ${MODULE} -am package -Dmaven.test.skip=true

FROM eclipse-temurin:17-jre
ARG MODULE
WORKDIR /app
ENV JAVA_TOOL_OPTIONS="-Xmx384m"
COPY --from=build /app/${MODULE}/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]