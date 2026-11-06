FROM dhi.io/amazoncorretto:27.0.1.0.11-debian13

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]