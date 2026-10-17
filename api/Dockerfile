FROM amazoncorretto:26.0.2-alpine3.24

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]