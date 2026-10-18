FROM dhi.io/amazoncorretto:26.0.2.11.1-debian13

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]