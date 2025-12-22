# 멀티 스테이지 빌드를 사용한 최적화된 Dockerfile
FROM gradle:8.5-jdk21 AS builder
WORKDIR /app
# Gradle 캐시를 위한 의존성 파일 복사
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
# 의존성 다운로드 (캐시 활용)
RUN gradle dependencies --no-daemon || true
# 소스 코드 복사
COPY src ./src
# 애플리케이션 빌드 (테스트 제외)
RUN gradle clean build -x test --no-daemon

# 실행 스테이지
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 타임존 설정
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
    echo "Asia/Seoul" > /etc/timezone && \
    apk del tzdata

# 보안을 위한 사용자 생성
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# 빌드된 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 헬스체크
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# JVM 옵션 설정
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java","-jar","/app/app.jar"]

