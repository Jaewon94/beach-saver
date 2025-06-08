-- 이 스크립트는 docker-compose의 environment 설정으로 'beachsaver_dev' DB와
-- 'beachsaver_developer' 유저가 자동으로 생성된 직후에 실행됩니다.

-- 1. 테스트용 데이터베이스를 생성합니다.
CREATE DATABASE IF NOT EXISTS beachsaver_test;

-- 2. docker-compose.yml이 자동으로 생성한 'beachsaver_developer' 유저에게
--    새로 만든 'beachsaver_test' 데이터베이스에 대한 모든 권한을 부여합니다.
--    호스트를 '%'로 지정하여 DBeaver와 같은 외부에서의 접속을 허용합니다.
GRANT ALL PRIVILEGES ON beachsaver_test.* TO 'beachsaver_developer'@'%';