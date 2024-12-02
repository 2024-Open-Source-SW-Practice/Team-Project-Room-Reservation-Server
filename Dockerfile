# Dockerfile
FROM openjdk:17-alpine
WORKDIR /app

# JAR 파일을 복사합니다.
COPY ./build/libs/TeamPu-0.0.1-SNAPSHOT.jar app.jar

# 애플리케이션을 실행합니다.
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=cd"]
