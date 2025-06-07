
## 환경 변수 설정 및 적용 방법

1. `.env.example` 파일을 복사해 `.env` 파일을 생성하세요.

   ```sh
   cp .env.example .env
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
