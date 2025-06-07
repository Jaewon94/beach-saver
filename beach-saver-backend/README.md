# BEACH-SAVER-BACKEND README

## 환경 변수 설정 및 적용 방법

1. `.env.template` 파일을 복사해 `.env.dev, .env.prod, .env.staging` 파일을 생성하세요.

   ```sh
   cp .env.example .env.dev
   ```

2. `.env` 파일의 `JWT_SECRET_KEY_DEV` 항목에 각자 고유의 비밀키를 입력하세요.

3. Spring Boot를 실행하기 전에 환경변수를 적용하세요.

   - 터미널에서:

     ```sh
     export $(cat .env | xargs)
     ./gradlew bootRun
     ```

   - 또는 IDE/CI 환경에서는 환경변수 설정 메뉴를 활용하세요.

4. `.env` 파일은 **절대 git에 커밋하지 마세요**. (이미 .gitignore에 추가되어 있음)

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
