# Dockerfile
FROM openjdk:17-alpine
WORKDIR /app

# JAR 파일을 복사합니다.
COPY ./build/libs/TeamPu-0.0.1-SNAPSHOT.jar app.jar

# 애플리케이션을 실행합니다.
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=cd"]

# 기존 Dockerfile 설정에 flyway 경로 추가
COPY src/main/resources/db/migration /flyway/sql/
COPY src/main/resources/application.yml /flyway/conf/
COPY src/main/resources/application-ci.yml /flyway/conf/
COPY src/main/resources/application-cd.yml /flyway/conf/

# Flyway 환경 변수 설정
ENV FLYWAY_URL=jdbc:mysql://${DB_HOST}/teampu
ENV FLYWAY_USER=root
ENV FLYWAY_PASSWORD=${DB_PASSWORD}
