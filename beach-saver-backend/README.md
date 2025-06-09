# BEACH-SAVER-BACKEND README

## 환경 변수 설정 및 적용 방법

1. **프로필별 환경변수 파일 준비**

   - 개발: `.env.dev`
   - 운영: `.env.prod`
   - 스테이징: `.env.staging`
   - 예시 파일: `.env.template` (필요 항목 참고)

   ```sh
   cp .env.template .env.dev
   # 필요시 .env.prod, .env.staging 등도 복사/생성
   ```

2. **각 환경변수 파일에 민감 정보(예: JWT_SECRET_KEY_DEV 등) 입력**

   - 각자 고유의 비밀키, DB 정보 등 입력

3. **Spring Boot 실행 시 자동으로 해당 프로필의 .env 파일이 로드됨**

   - 현재 스프링 부트 실행시 .env 파일을 바로 적용하기 위해 **dotenv** 라이브러리 사용 중
   - `spring.profiles.active` 값에 따라 `.env.{profile}` 파일 자동 적용 (main.java 확인)
   - 예시:
     - 개발 환경: 기본값(dev) → `.env.dev` 자동 로드
     - 운영 환경: `SPRING_PROFILES_ACTIVE=prod` 또는 `-Dspring.profiles.active=prod` 지정 시 `.env.prod` 자동 로드

   ```sh
   # 개발 환경(기본)
   ./gradlew bootRun

   # 운영 환경
   SPRING_PROFILES_ACTIVE=prod ./gradlew bootRun
   # 또는
   ./gradlew bootRun -Dspring.profiles.active=prod
   ```

4. **.env 파일은 절대 git에 커밋하지 마세요**
   - 이미 .gitignore에 추가되어 있음

---

## 코드 스타일 & 자동 포맷팅 정책

### 1. 코드 스타일

- **Google Java Format**(구글 공식 자바 스타일) 강제 적용
- 저장 시 VSCode에서 자동 포맷, 커밋 전 spotless로 이중 적용

### 2. 자동화 도구

- **spotless**: Gradle 기반 코드 포맷터(google-java-format)
- **checkstyle**: 코드 스타일 검사
- **husky + lint-staged**: 커밋 전 자동 포맷(spotlessApply) 실행

### 3. 개발 환경 세팅 및 커밋 훅 적용법

#### 필수 설치

- **Java 17+**
- **Node.js 18+** (husky/lint-staged용)
- **npm** (Node.js 설치 시 자동 포함)
- **Gradle** (로컬 설치 or Wrapper 사용)

#### 프로젝트 클론 후 최초 1회만

```sh
# 1. 의존성 설치 (Node.js 기반 도구)
npm install

# 2. husky 디렉토리/훅 자동 생성 (최초 1회만)
npx husky init

# 3. pre-commit 훅에 lint-staged 연결 (경고 무시해도 됨)
npx husky add .husky/pre-commit "npx lint-staged"
```

#### VSCode 설정

`.vscode/settings.json`에 아래 내용이 포함되어야 함(이미 포함되어 있으면 생략):

```json
{
  "editor.formatOnSave": true,
  "java.format.settings.url": "https://raw.githubusercontent.com/google/styleguide/gh-pages/eclipse-java-google-style.xml",
  "java.format.settings.profile": "GoogleStyle"
}
```

- 위와 같이 설정시 저장시 코드 스타일 적용

#### 커밋 전 자동 포맷 동작 방식

- Java 파일을 수정/저장하면 VSCode에서 자동 포맷
- git add 후 커밋 시, husky + lint-staged가 `./gradlew spotlessApply`를 실행하여 코드 스타일을 강제 적용
- 포맷이 맞지 않으면 커밋이 중단되고, 자동 포맷 후 다시 git add 필요

#### 전체 코드 일괄 포맷(필요시)

```sh
./gradlew spotlessApply
```

### 신규 팀원이 해야 할 것 (요약)

1. Java, Node.js, npm 설치
2. `npm install`
3. `npx husky init`
4. `npx husky add .husky/pre-commit "npx lint-staged"`
5. VSCode 설정 확인/적용
6. 개발 및 커밋 시 자동 포맷 정상 동작 확인

### 참고/주의

- 커밋 훅은 로컬에서만 동작하므로, 반드시 위 과정을 거쳐야 코드 스타일이 통일됨
- IDE 포맷터와 spotless가 완전히 동일하지 않을 수 있으니, 커밋 전 spotless가 최종 기준
- 대형 프로젝트는 커밋 전 spotlessApply가 느릴 수 있음(필요시 lint-staged로 변경된 파일만 적용)

## 로컬 개발 환경: Docker로 DB(MySQL) 실행 및 연동 방법

### 1. MySQL 컨테이너 실행

```sh
docker-compose up -d
```

- 위 명령으로 로컬에 MySQL 8.0 컨테이너가 실행됨
- 데이터는 `./mysql-data` 폴더에 영구 저장됨(컨테이너 삭제해도 데이터 유지)

### 2. DB 계정/환경변수 설정

- `docker-compose.yml`의 DB 계정정보와 `application-dev.yml`(Spring Boot 설정) 내 DB 접속 정보가 반드시 일치해야 함
  - 예시:
    - username: `beachsaver_developer`
    - password: `beachsaver_developer`
    - database: `beachsaver_dev`
- JWT 등 민감 정보는 `.env.dev` 등 환경변수 파일에 별도 관리

### 3. Spring Boot 실행 전 환경변수 적용

- 현재 스프링 부트 실행시 .env 파일을 바로 적용하기 위해 **dotenv** 라이브러리 사용 중
  - `spring.profiles.active` 값에 따라 `.env.{profile}` 파일 자동 적용 (main.java 확인)
  - 예시:
    - 개발 환경: 기본값(dev) → `.env.dev` 자동 로드
    - 운영 환경: `SPRING_PROFILES_ACTIVE=prod` 또는 `-Dspring.profiles.active=prod` 지정 시 `.env.prod` 자동 로드

### 4. 포트/볼륨/환경변수 커스터마이즈

- MySQL 기본 포트(3306) 충돌 시, `docker-compose.yml`의 `ports` 항목 수정
- 데이터 볼륨 경로(`./mysql-data`)는 필요에 따라 변경 가능
- 추가 환경변수는 `env_file` 또는 `environment` 항목에 직접 지정

### 5. 주의/팁

- DB 계정정보 불일치 시 Spring Boot에서 DB 접속 오류 발생
- 컨테이너가 정상 실행 중인지 확인하려면:

  ```sh
  docker ps
  ```

- 컨테이너 로그 확인:

  ```sh
  docker logs beachsaver-mysql-dev
  ```

- 컨테이너 중지/삭제:

  ```sh
  docker-compose down
  ```

### 6. 전체 워크플로우 요약

1. `docker-compose up -d`로 MySQL 컨테이너 실행
2. `.env.dev` 등 환경변수 파일 준비 및 적용
3. Spring Boot 실행(`./gradlew bootRun`)
4. DB/애플리케이션 연동 정상 동작 확인

## 코드 테이블 관리 정책

### 1. enum 관리

- **report.type**: 'Investigation', 'Cleaning', 'Collection_Request'만 사용(수거 완료는 별도 보고서 생성 X)
- **collection_depot.status**: 'WAITING'(수거대기), 'COMPLETED'(수거완료) 등 상태값으로만 관리

### 2. 코드 테이블/체크 동시 반영 필수

- enum 값 추가/변경 시 코드 테이블/체크 동시 반영 필수
- 수거 완료는 collection_depot.status만 변경, 별도 보고서 생성/분리 X

## 데이터 삭제(Soft Delete vs Hard Delete) 정책

### 1. 논리 삭제(Soft Delete)

- **deleted_at 컬럼**을 사용하여 실제 데이터를 삭제하지 않고, 삭제 시각만 기록
- 복구, 이력 감사, 운영 품질 보장 등 실무적으로 중요한 테이블에만 적용
- 적용 대상: `user`, `report`, `team`, `collection_depot` 등
- JPA 엔티티에도 `deletedAt`(LocalDateTime) 필드 필수
- 실제 삭제는 하지 않으므로, 쿼리/비즈니스 로직에서 `deleted_at IS NULL` 조건 필수

### 2. 하드 딜리트(Hard Delete)

- DB에서 실제로 데이터를 삭제
- 임시/로그/참조 무관 데이터 등, 복구/감사 필요 없는 테이블에만 적용
- 예시: `log`, `notification`, `user_admin`, `user_worker`, 각종 이미지 테이블 등

### 3. 정책 적용 기준

- 어떤 테이블에 논리 삭제를 적용할지는 **DB 설계서/아키텍처 문서/README**에 명확히 표기
- 논리 삭제 대상 테이블은 설계서/DDL/코드에 반드시 `deleted_at` 컬럼 포함
- 하드 딜리트 대상은 별도 컬럼 없이 실제 삭제

### 4. 실무적 주의

- 논리 삭제된 데이터는 SELECT 시 기본적으로 제외해야 함(예: `WHERE deleted_at IS NULL`)
- 복구/감사/통계 등 특수 목적 쿼리에서는 논리 삭제 데이터도 조회 가능
- 논리 삭제 정책은 운영/보안/감사 품질에 직접적 영향, 반드시 일관성 유지

> 정책 위반 시 데이터 유실, 감사 불가, 운영 혼선 등 심각한 문제 발생 가능
