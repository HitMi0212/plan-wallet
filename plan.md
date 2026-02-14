# plan-wallet 개발 계획서 (Kotlin + Spring)

## 0. 개요

-   프로젝트명: **plan-wallet**
-   목적: iOS/Android 가계부 앱에서 사용할 백엔드(API) 구축
-   아키텍처: Layered Architecture
-   개발 방식: TDD 기반 개발 (Mock 사용)

------------------------------------------------------------------------

## 1. 기술 스택

### Backend

-   Language: Kotlin (2.3.10)
-   Framework: Spring Boot (4.0.2)
-   Build: Gradle (Kotlin DSL)
-   API: REST (JSON) / (옵션) GraphQL
-   Auth: Spring Security + JWT
-   ORM: Spring Data JPA (Hibernate)
-   Validation: javax/jakarta validation
-   Docs: springdoc-openapi (Swagger UI)

### Infra

-   DB: MySQL
-   Cache/Session(옵션): Redis
-   File Storage(옵션): S3 호환 스토리지
-   Container: Docker / Docker Compose
-   CI/CD: GitHub Actions

------------------------------------------------------------------------

## 2. API Prefix 규칙

모든 엔드포인트는 `/plan` 으로 시작한다.

예시: - POST /plan/auth/login - GET /plan/transactions - GET
/plan/stats/monthly

------------------------------------------------------------------------

## 3. 아키텍처 구조 (Layered)

presentation → application → domain → infrastructure

### 패키지 구조 예시

src/main/kotlin/com/planwallet/ presentation/ application/ domain/
infrastructure/ common/

------------------------------------------------------------------------

## 4. 도메인 모델 (초안)

### User

-   id
-   email
-   passwordHash
-   nickname
-   createdAt
-   updatedAt

### Category

-   id
-   userId
-   type (INCOME/EXPENSE)
-   name
-   isDeleted
-   createdAt
-   updatedAt

### Transaction

-   id
-   userId
-   type (INCOME/EXPENSE)
-   amount (Long, 원 단위 저장)
-   categoryId
-   memo
-   occurredAt
-   createdAt
-   updatedAt

------------------------------------------------------------------------

## 5. 테스트 전략 (TDD + Mock)

-   구현 전 테스트 작성
-   Service 레이어 중심 단위 테스트
-   Repository 및 외부 의존성은 Mock 처리
-   Controller는 WebMvcTest 사용
-   Mockito 또는 MockK 사용

------------------------------------------------------------------------

## 6. 보안 설계

-   BCrypt로 비밀번호 해시 저장
-   JWT 기반 인증
    -   Access Token: 15~30분
    -   Refresh Token: 14~30일
-   CORS 제한 설정

------------------------------------------------------------------------

## 7. 데이터베이스 설계

### 테이블

-   users
-   categories
-   transactions
-   refresh_tokens (옵션)

### 인덱스 권장

-   transactions(user_id, occurred_at)
-   transactions(user_id, category_id, occurred_at)
-   categories(user_id, type)

------------------------------------------------------------------------

## 8. 배포 전략

-   Docker 기반 컨테이너화
-   Docker Compose로 MySQL/Redis 구성
-   GitHub Actions로 CI/CD
-   환경 분리: local / dev / prod

------------------------------------------------------------------------

## 9. 개발 일정 (예시)

### Sprint 1

-   프로젝트 세팅
-   JWT 인증 구현
-   User 기능

### Sprint 2

-   Category CRUD
-   Transaction CRUD

### Sprint 3

-   통계 API
-   테스트 보강
-   Swagger 정리

### Sprint 4

-   Docker 구성
-   CI/CD 구축
-   운영 설정 정리

------------------------------------------------------------------------

## 10. 확장 고려사항

-   정기 거래
-   예산 기능
-   푸시 알림
-   다중 통화 지원
-   GraphQL 도입 여부 검토

------------------------------------------------------------------------

END
