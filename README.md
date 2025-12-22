# 🎯 세종대학교 해커톤 백엔드

세종대학교 해커톤을 위한 Spring Boot 기반 REST API 서버입니다.

## 📋 목차
- [기술 스택](#-기술-스택)
- [주요 기능](#-주요-기능)
- [시작하기](#-시작하기)
- [API 문서](#-api-문서)
- [배포](#-배포)
- [프로젝트 구조](#-프로젝트-구조)

## 🛠 기술 스택

### Backend
- **Java 21**
- **Spring Boot 3.5.5**
- **Spring Security** - JWT 기반 인증/인가
- **Spring Data JPA** + **QueryDSL** - 데이터베이스 접근
- **PostgreSQL** - 메인 데이터베이스
- **Redis** - 세션 관리 (예정)

### DevOps
- **Docker** + **Docker Compose** - 컨테이너화
- **AWS ECR** - Docker 이미지 저장소
- **AWS EC2** - 서버 호스팅
- **GitHub Actions** - CI/CD 파이프라인

### 기타
- **Swagger/OpenAPI** - API 문서화
- **Lombok** - 코드 간소화
- **P6Spy** - SQL 쿼리 로깅
- **Sejong Portal Login** - 세종대 포털 로그인 연동

## ✨ 주요 기능

### 🔐 인증 (Authentication)
- **세종대학교 포털 로그인** 연동
  - 세종대 포털 계정으로 간편 로그인
  - 최초 로그인 시 회원 정보 자동 생성
- **JWT 기반 인증**
  - Access Token / Refresh Token
  - Stateless 인증 방식

### 👤 회원 관리 (Member)
- 내 정보 조회
- 회원 정보 자동 동기화 (포털 연동)
- 회원 상태 관리 (ACTIVE, INACTIVE, BANNED)

### 🔒 보안
- Spring Security 기반 보안 설정
- JWT 토큰 인증/인가
- CORS 설정
- 비밀번호 암호화 (BCrypt)

## 🚀 시작하기

### 사전 요구사항
- Java 21
- Docker & Docker Compose
- PostgreSQL 14+
- Redis (선택)

### 로컬 개발 환경 설정

1. **레포지토리 클론**
```bash
git clone https://github.com/2025-Sejong-Hackathon/hackathon-backend.git
cd hackathon-backend
```

2. **환경 변수 설정**
```bash
# src/main/resources/application-dev.yml 참고
# 또는 환경 변수로 설정

export DATASOURCE_URL=jdbc:postgresql://localhost:5432/hackathon
export DATASOURCE_USERNAME=your_username
export DATASOURCE_PASSWORD=your_password
export REDIS_HOST=localhost
export REDIS_PORT=6379
export JWT_SECRET=your-secret-key-min-32-characters
export JWT_ACCESS_TOKEN_EXPIRATION=3600000
export JWT_REFRESH_TOKEN_EXPIRATION=604800000
```

3. **데이터베이스 준비**
```bash
# Docker로 PostgreSQL 실행
docker run -d \
  --name hackathon-postgres \
  -e POSTGRES_DB=hackathon \
  -e POSTGRES_USER=hackathon \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 \
  postgres:14
```

4. **애플리케이션 실행**
```bash
# Gradle 빌드 및 실행
./gradlew bootRun --args='--spring.profiles.active=dev'
```

5. **API 문서 확인**
```
http://localhost:8080/swagger-ui.html
```

## 📚 API 문서

### Swagger UI
개발 환경에서는 Swagger UI를 통해 API를 테스트할 수 있습니다.
- **URL**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Spec**: `http://localhost:8080/api-docs`

### 주요 엔드포인트

#### 인증 API
```
POST /api/v1/auth/login          # 세종대 포털 로그인
POST /api/v1/auth/refresh        # 토큰 갱신
```

#### 회원 API
```
GET  /api/v1/members/me          # 내 정보 조회
```

#### 헬스체크
```
GET  /actuator/health            # 서버 상태 확인
```

## 🐳 Docker로 실행

### Docker Compose 사용
```bash
# 환경 변수 설정
export ECR_REGISTRY=your-account.dkr.ecr.ap-northeast-2.amazonaws.com
export ECR_REPO=hackathon-backend

# .env 파일 생성 (docker-compose.yml과 같은 위치)
cat > .env << EOF
SPRING_PROFILES_ACTIVE=prod
DATASOURCE_URL=jdbc:postgresql://your-db-host:5432/hackathon
DATASOURCE_USERNAME=your_username
DATASOURCE_PASSWORD=your_password
REDIS_HOST=your-redis-host
REDIS_PORT=6379
JWT_SECRET=your-secret-key
JWT_ACCESS_EXPIRATION=3600000
JWT_REFRESH_EXPIRATION=604800000
EOF

# 컨테이너 실행
docker-compose up -d
```

### Docker 직접 사용
```bash
# 이미지 빌드
docker build -t hackathon-backend .

# 컨테이너 실행
docker run -d \
  -p 8082:8080 \
  --env-file .env \
  --name hackathon-backend \
  hackathon-backend
```

## 🚢 배포

### GitHub Actions CI/CD

자동 배포 파이프라인이 구성되어 있습니다.

1. **`main` 브랜치에 Push** → 자동 배포 시작
2. **빌드** → Docker 이미지 생성
3. **Push to ECR** → AWS ECR에 이미지 업로드
4. **Deploy to EC2** → SSH로 EC2 접속하여 배포

### 배포 환경 변수 (GitHub Secrets)

GitHub Repository → Settings → Secrets and variables → Actions에 등록:

```
AWS_ACCOUNT_ID       # AWS 계정 ID
EC2_HOST             # EC2 인스턴스 IP/도메인
EC2_USER             # SSH 사용자명 (ubuntu)
EC2_KEY              # SSH 프라이빗 키 (PEM 파일 내용)
```

### 수동 배포

```bash
# 1. 이미지 빌드
./gradlew clean build -x test
docker build -t hackathon-backend .

# 2. ECR에 Push
aws ecr get-login-password --region ap-northeast-2 | \
  docker login --username AWS --password-stdin ${ECR_REGISTRY}
docker tag hackathon-backend:latest ${ECR_REGISTRY}/hackathon-backend:latest
docker push ${ECR_REGISTRY}/hackathon-backend:latest

# 3. EC2에서 Pull & 실행
ssh ubuntu@your-ec2-host
docker pull ${ECR_REGISTRY}/hackathon-backend:latest
docker-compose up -d
```

자세한 배포 가이드는 [DEPLOYMENT_NOTES.md](./DEPLOYMENT_NOTES.md)를 참고하세요.

## 📁 프로젝트 구조

```
src/
├── main/
│   ├── java/com/hackathon/backend/
│   │   ├── api/                    # API Layer (Controller, DTO)
│   │   │   ├── auth/              # 인증 API
│   │   │   └── member/            # 회원 API
│   │   ├── domain/                # Domain Layer (Service, Entity, Repository)
│   │   │   ├── auth/              # 인증 도메인
│   │   │   └── member/            # 회원 도메인
│   │   ├── global/                # Global 설정 및 유틸리티
│   │   │   ├── aop/               # AOP (로깅 등)
│   │   │   ├── config/            # 설정 클래스
│   │   │   ├── entity/            # BaseEntity 등
│   │   │   ├── exception/         # 예외 처리
│   │   │   ├── jwt/               # JWT 유틸리티
│   │   │   ├── response/          # 공통 응답 형식
│   │   │   └── security/          # Spring Security 설정
│   │   └── config/                # 메인 설정 파일
│   │       ├── JwtProperties.java
│   │       ├── SecurityConfig.java
│   │       ├── SwaggerConfig.java
│   │       └── WebMvcConfig.java
│   └── resources/
│       ├── application.yml         # 공통 설정
│       ├── application-dev.yml     # 개발 환경
│       ├── application-prod.yml    # 운영 환경
│       └── spy.properties          # P6Spy 설정
└── test/                           # 테스트 코드
```

## 🏗 아키텍처

### 레이어 구조 (Layered Architecture)

```
┌─────────────────────────────────┐
│     API Layer (Controller)      │  ← HTTP 요청/응답 처리
├─────────────────────────────────┤
│   Domain Layer (Service)        │  ← 비즈니스 로직
├─────────────────────────────────┤
│ Infrastructure Layer (Repository)│  ← 데이터 접근
└─────────────────────────────────┘
```

### 인증 흐름

```
Client → Controller → Security Filter → JWT Validation 
  ↓                                           ↓
Response ← Service ← Repository ← Load User Data
```

## 🔧 설정 파일

### application.yml
- **공통 설정**: 모든 프로파일에 적용
- **JWT 설정**: 토큰 만료 시간 등
- **Logging 설정**: 로그 레벨

### application-dev.yml
- **개발 환경 설정**
- `ddl-auto: update` - 스키마 자동 업데이트
- `show-sql: true` - SQL 쿼리 로깅
- Swagger UI 활성화

### application-prod.yml
- **운영 환경 설정**
- `ddl-auto: validate` - 스키마 검증만 수행
- `show-sql: false` - SQL 쿼리 로깅 비활성화
- Swagger UI 비활성화
- Connection Pool 최적화

## 🧪 테스트

```bash
# 전체 테스트 실행
./gradlew test

# 특정 테스트 실행
./gradlew test --tests com.hackathon.backend.domain.auth.service.AuthServiceTest

# 테스트 리포트 확인
open build/reports/tests/test/index.html
```

## 📝 개발 가이드

### 코드 스타일
- **Lombok** 활용으로 보일러플레이트 코드 최소화
- **Builder 패턴** 사용 (Entity 생성)
- **LayeredArchitecture** 준수
- **RESTful API** 설계 원칙 준수

### 브랜치 전략
- `main`: 운영 환경 (자동 배포)
- `develop`: 개발 환경
- `feature/*`: 기능 개발
- `hotfix/*`: 긴급 수정

### 커밋 메시지 컨벤션
```
feat: 새로운 기능 추가
fix: 버그 수정
docs: 문서 수정
refactor: 코드 리팩토링
test: 테스트 코드 추가
chore: 빌드 설정, 패키지 매니저 수정
```

## 🐛 트러블슈팅

### 자주 발생하는 문제

1. **데이터베이스 연결 실패**
   - PostgreSQL이 실행 중인지 확인
   - `DATASOURCE_URL` 환경 변수 확인

2. **JWT 토큰 오류**
   - `JWT_SECRET`이 최소 32자 이상인지 확인
   - 토큰 만료 시간 확인

3. **세종대 포털 로그인 실패**
   - 세종대 포털 서버 상태 확인
   - 학번/비밀번호 정확성 확인

자세한 문제 해결은 [DEPLOYMENT_NOTES.md](./DEPLOYMENT_NOTES.md)를 참고하세요.

## 👥 팀

**2025 세종대학교 해커톤 백엔드 팀**

## 📄 라이선스

This project is licensed under the MIT License.

## 🔗 관련 링크

- [세종대학교](https://www.sejong.ac.kr)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Docker Documentation](https://docs.docker.com)

---

**Made with ❤️ by Sejong Hackathon Team**

