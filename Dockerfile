# Dockerfile
FROM openjdk:17-alpine
WORKDIR /app

EXPOSE 8080

# JAR 파일을 복사합니다.
COPY ./build/libs/TeamPu-0.0.1-SNAPSHOT.jar app.jar

# 애플리케이션을 실행합니다.
ENTRYPOINT ["java", "-jar", "app.jar"]
