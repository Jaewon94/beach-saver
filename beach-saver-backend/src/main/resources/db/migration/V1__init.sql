-- =========================
-- Beach-Saver 초기 DB 스키마 (Flyway V1)
-- =========================

-- =========================
-- ENUM/코드 테이블 정의 및 샘플 데이터
-- =========================

-- 사용자 역할 코드
CREATE TABLE code_user_role (
  code VARCHAR(20) PRIMARY KEY,
  description VARCHAR(100)
);
INSERT INTO code_user_role (code, description) VALUES
  ('PlatformAdmin', '플랫폼 전체 관리자'),
  ('CityAdmin', '시/도 관리자'),
  ('DistrictAdmin', '구/군 관리자'),
  ('Investigator', '조사자'),
  ('Cleaner', '청소자'),
  ('Collector', '수거자'),
  ('Citizen', '시민 자원봉사자');

-- 사용자 상태 코드
CREATE TABLE code_user_status (
  code VARCHAR(20) PRIMARY KEY,
  description VARCHAR(100)
);
INSERT INTO code_user_status (code, description) VALUES
  ('ACTIVE', '활성'),
  ('SUSPENDED', '정지'),
  ('WITHDRAWN', '탈퇴');

-- 보고서 유형 코드
CREATE TABLE code_report_type (
  code VARCHAR(30) PRIMARY KEY,
  description VARCHAR(100)
);
INSERT INTO code_report_type (code, description) VALUES
  ('Investigation', '조사 보고서'),
  ('Cleaning', '청소 보고서'),
  ('Collection_Request', '수거용(요청) 보고서(청소자 작성)');

-- 보고서 상태 코드
CREATE TABLE code_report_status (
  code VARCHAR(20) PRIMARY KEY,
  description VARCHAR(100)
);
INSERT INTO code_report_status (code, description) VALUES
  ('DRAFT', '임시'),
  ('SUBMITTED', '제출'),
  ('APPROVED', '승인'),
  ('REJECTED', '반려');

-- 이미지 유형 코드
CREATE TABLE code_image_type (
  code VARCHAR(30) PRIMARY KEY,
  description VARCHAR(100)
);
INSERT INTO code_image_type (code, description) VALUES
  ('INVESTIGATION_MAIN', '조사 메인 이미지'),
  ('INVESTIGATION_AREA', '조사 구역 이미지'),
  ('CLEANING_BEFORE_AREA', '청소 전 구역 이미지'),
  ('CLEANING_AFTER_AREA', '청소 후 구역 이미지'),
  ('COLLECTION_DEPOT', '임시 집하장(수거용) 이미지'),
  ('COLLECTION_COMPLETED', '수거 완료 이미지');

-- 알림 유형 코드
CREATE TABLE code_notification_type (
  code VARCHAR(10) PRIMARY KEY,
  description VARCHAR(100)
);
INSERT INTO code_notification_type (code, description) VALUES
  ('PUSH', '푸시 알림'),
  ('SMS', '문자'),
  ('EMAIL', '이메일');

-- 알림 상태 코드
CREATE TABLE code_notification_status (
  code VARCHAR(10) PRIMARY KEY,
  description VARCHAR(100)
);
INSERT INTO code_notification_status (code, description) VALUES
  ('UNREAD', '안읽음'),
  ('READ', '읽음');

-- =========================
-- 실제 데이터 테이블 정의 (CHECK 제약조건 병행)
-- =========================

-- 조직(시/도/구/군 등)
CREATE TABLE organization (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 조직 고유 ID
  name VARCHAR(100) NOT NULL,            -- 조직명
  type VARCHAR(20) NOT NULL,             -- 조직유형(Platform/City/District)
  parent_id BIGINT,                      -- 상위 조직 FK(선택)
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- 생성일시
  FOREIGN KEY (parent_id) REFERENCES organization(id)
);

-- 사용자 테이블
CREATE TABLE user (
  id BIGINT AUTO_INCREMENT PRIMARY KEY, -- PK: 사용자 고유 ID
  system_id VARCHAR(50) UNIQUE NOT NULL, -- 시스템 자동 생성 ID(내부 식별자, 로그인/감사/통계용)
  nickname VARCHAR(50),                  -- 사용자가 입력하는 닉네임(표시명, 중복 허용)
  phone VARCHAR(20) UNIQUE,              -- 휴대폰 번호(로그인/식별자, UNIQUE)
  password VARCHAR(255) NOT NULL,        -- 비밀번호(암호화 저장)
  role VARCHAR(20) NOT NULL,
    CHECK (role IN ('PlatformAdmin','CityAdmin','DistrictAdmin','Investigator','Cleaner','Collector','Citizen')),
  org_id BIGINT,                         -- 소속 조직 FK
  status VARCHAR(20) DEFAULT 'ACTIVE',
    CHECK (status IN ('ACTIVE','SUSPENDED','WITHDRAWN')),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- 생성일시
  updated_at DATETIME,                   -- 수정일시
  FOREIGN KEY (org_id) REFERENCES organization(id)
);

-- 팀(실무 단위)
CREATE TABLE team (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 팀 고유 ID
  name VARCHAR(50) NOT NULL,             -- 팀명
  org_id BIGINT,                         -- 소속 조직 FK
  leader_id BIGINT,                      -- 팀장(사용자) FK
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- 생성일시
  FOREIGN KEY (org_id) REFERENCES organization(id),
  FOREIGN KEY (leader_id) REFERENCES user(id)
);

-- 사용자-팀(N:M) 이력 관리 테이블
CREATE TABLE user_team (
  user_id BIGINT,                        -- 사용자 FK
  team_id BIGINT,                        -- 팀 FK
  joined_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- 팀 소속 시작일(PK)
  left_at DATETIME,                      -- 팀 소속 종료일(탈퇴)
  role_in_team VARCHAR(20),              -- 팀 내 역할(선택)
  status VARCHAR(20) DEFAULT 'ACTIVE',
    CHECK (status IN ('ACTIVE','SUSPENDED','WITHDRAWN')),
  PRIMARY KEY (user_id, team_id, joined_at), -- 한 사용자가 동일 팀에 여러 번 소속될 수 있음 (퇴사 후 다시 소속될 수 있음)
  FOREIGN KEY (user_id) REFERENCES user(id),
  FOREIGN KEY (team_id) REFERENCES team(id)
);

-- 관리자(행정 담당자) 특화 정보
CREATE TABLE user_admin (
  user_id BIGINT PRIMARY KEY,            -- user 테이블과 1:1
  work_city VARCHAR(20),                 -- 일하는 지역
  work_place VARCHAR(40),                -- 근무처
  department VARCHAR(20),                -- 부서
  position VARCHAR(20),                  -- 직급
  contact VARCHAR(20) UNIQUE,            -- 근무처 연락처
  FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 근로자(조사자/청소자/수거자) 특화 정보
CREATE TABLE user_worker (
  user_id BIGINT PRIMARY KEY,            -- user 테이블과 1:1
  vehicle_capacity DOUBLE,               -- 차량정보
  birth DATE,                            -- 생년월일
  start_date DATE,                       -- 근무 시작일
  end_date DATE,                         -- 근무 종료일
  FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 해변
CREATE TABLE beach (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 해변 고유 ID
  org_id BIGINT NOT NULL,                -- 소속 조직 FK
  beach_name VARCHAR(50) NOT NULL,       -- 해변명(조직 내 고유)
  si VARCHAR(30) NOT NULL,               -- 시
  gu_gun VARCHAR(30) NOT NULL,           -- 구/군
  dong_eub VARCHAR(30) NOT NULL,         -- 동/읍
  workplace VARCHAR(30) NOT NULL,        -- 근무처(행정구역 단위)
  latitude DOUBLE NOT NULL,              -- 위도
  longitude DOUBLE NOT NULL,             -- 경도
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- 생성일시
  UNIQUE(org_id, beach_name),            -- 조직 내 해변명 유니크
  FOREIGN KEY (org_id) REFERENCES organization(id)
);

-- 보고서(공통)
CREATE TABLE report (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 보고서 고유 ID
  type VARCHAR(30) NOT NULL,
    CHECK (type IN ('Investigation','Cleaning','Collection_Request')),
  user_id BIGINT,                        -- 대표 작성자/책임자 FK
  team_id BIGINT,                        -- 소속 팀 FK(선택)
  status VARCHAR(20) DEFAULT 'DRAFT',
    CHECK (status IN ('DRAFT','SUBMITTED','APPROVED','REJECTED')),
  gps_lat DECIMAL(10,7),                 -- GPS 위도
  gps_lng DECIMAL(10,7),                 -- GPS 경도
  content TEXT,                          -- 상세 내용
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- 생성일시
  updated_at DATETIME,                   -- 수정일시
  FOREIGN KEY (user_id) REFERENCES user(id),
  FOREIGN KEY (team_id) REFERENCES team(id)
);

-- 조사 메인 보고서
CREATE TABLE report_investigation (
  report_id BIGINT PRIMARY KEY,          -- report FK
  beach_id BIGINT,                       -- 해변 FK
  investigation_at DATETIME,             -- 조사 일시
  disaster_type VARCHAR(20),             -- 자연재해 유무/종류
  weather VARCHAR(20),                   -- 날씨(자동/수동)
  note VARCHAR(255),                     -- 특이사항/비고
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- 생성일시
  updated_at DATETIME,                   -- 수정일시
  FOREIGN KEY (report_id) REFERENCES report(id),
  FOREIGN KEY (beach_id) REFERENCES beach(id)
);

-- 조사 구역별/서브 보고서
CREATE TABLE report_investigation_area (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 구역 조사 고유 ID
  report_id BIGINT NOT NULL,             -- 메인 보고서 ID (report.id 직접 참조)
  area_name VARCHAR(100),                -- 구역명/구간명
  start_latitude DECIMAL(10,7),          -- 구역 시작 위도
  start_longitude DECIMAL(10,7),         -- 구역 시작 경도
  end_latitude DECIMAL(10,7),            -- 구역 끝 위도
  end_longitude DECIMAL(10,7),           -- 구역 끝 경도
  trash_amount_est INTEGER,              -- 예상 쓰레기 양
  main_trash_type VARCHAR(50),           -- 주 쓰레기 종류
  note VARCHAR(255),                     -- 구역 특이사항/비고
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- 생성일시
  updated_at DATETIME,                   -- 수정일시
  FOREIGN KEY (report_id) REFERENCES report(id)
);

-- 청소 메인 보고서
CREATE TABLE report_cleaning (
  report_id BIGINT PRIMARY KEY,          -- report FK
  investigation_report_id BIGINT,        -- 연계된 조사 메인 보고서 ID (report.id 직접 참조)
  beach_id BIGINT,                       -- 해변 FK
  cleaning_at DATETIME,                  -- 청소 일시
  weather VARCHAR(20),                   -- 날씨(자동/수동)
  note VARCHAR(255),                     -- 특이사항/비고
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- 생성일시
  updated_at DATETIME,                   -- 수정일시
  FOREIGN KEY (report_id) REFERENCES report(id),
  FOREIGN KEY (investigation_report_id) REFERENCES report(id),
  FOREIGN KEY (beach_id) REFERENCES beach(id)
);

-- 청소 구역별/서브 보고서
CREATE TABLE report_cleaning_area (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 구역 청소 고유 ID
  report_id BIGINT NOT NULL,             -- 메인 청소 보고서 ID (report.id 직접 참조)
  investigation_area_id BIGINT,          -- 연계된 조사 구역별 FK(선택)
  area_name VARCHAR(100),                -- 구역명/구간명
  start_latitude DECIMAL(10,7),          -- 구역 시작 위도
  start_longitude DECIMAL(10,7),         -- 구역 시작 경도
  end_latitude DECIMAL(10,7),            -- 구역 끝 위도
  end_longitude DECIMAL(10,7),           -- 구역 끝 경도
  trash_amount INTEGER,                  -- 실제 수거 쓰레기 양
  main_trash_type VARCHAR(50),           -- 주 쓰레기 종류
  note VARCHAR(255),                     -- 구역 특이사항/비고
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- 생성일시
  updated_at DATETIME,                   -- 수정일시
  FOREIGN KEY (report_id) REFERENCES report(id),
  FOREIGN KEY (investigation_area_id) REFERENCES report_investigation_area(id)
);

-- 임시 집하장/수거용 포인트
--
-- 실무적 근거:
-- - 집하장(임시 집하장) 정보(사진, 위경도, 주소 등)는 수거자가 실제 현장에 도달하고, 정확히 수거 작업을 수행하기 위해 필수
--   (네비게이션, 지도 연동, 현장 확인, 오인/오배송 방지, 감사/통계/민원 대응 등)
-- - 수거자는 해당 집하장(및 연결된 보고서)에서 '완료' 버튼을 눌러 status를 'COMPLETED'로 변경, 완료자/완료시각/수거량 등 이력 기록
-- - 별도 '수거 완료 보고서' 생성 없이 단일 엔티티/상태 전이로 관리(데이터 중복/복잡성 최소화, 감사/통계/운영 효율성↑)
-- - 사진/위치 정보 누락 시 현장 오인/오배송, 완료 이력 미기록 시 감사/통계/운영상 문제 발생
CREATE TABLE collection_depot (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 임시 집하장 고유 ID
  collection_request_report_id BIGINT,   -- 수거용(요청) 보고서 FK
  name VARCHAR(100),                     -- 집하장명(임시, 청소자가 현장에 지정)
  address VARCHAR(255),                  -- 집하장 주소(임시, 네비/현장 확인용)
  latitude DECIMAL(10,7),                -- 위도(정확한 현장 위치 지정)
  longitude DECIMAL(10,7),               -- 경도(정확한 현장 위치 지정)
  status VARCHAR(20) NOT NULL,           -- 상태(Enum: WAITING/COMPLETED, 상태값만으로 관리)
    CHECK (status IN ('WAITING','COMPLETED')),
  collector_id BIGINT,                   -- 수거자(수거 완료 처리자) FK(누가 완료했는지 명확히)
  collection_completed_at DATETIME,      -- 수거 완료 시각(감사/통계/운영)
  collected_amount INTEGER,              -- 수거량(kg 등, 통계/운영)
  note VARCHAR(255),                     -- 특이사항/비고(현장 특이사항 등)
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- 생성일시
  updated_at DATETIME,                   -- 수정일시
  FOREIGN KEY (collection_request_report_id) REFERENCES report(id),
  FOREIGN KEY (collector_id) REFERENCES user(id)
);

-- 집하장 이미지 (1:N)
CREATE TABLE collection_depot_image (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 이미지 고유 ID
  collection_depot_id BIGINT NOT NULL,   -- 집하장(포인트) FK
  type VARCHAR(30) NOT NULL,
    CHECK (type IN ('COLLECTION_DEPOT','COLLECTION_COMPLETED')),
  file_name VARCHAR(255) NOT NULL,       -- S3 파일명
  ord INTEGER DEFAULT 0,                 -- 이미지 순서
  description VARCHAR(255),              -- 사진 설명(선택)
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (collection_depot_id) REFERENCES collection_depot(id)
);

-- 조사 보고서 이미지
CREATE TABLE report_investigation_image (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 이미지 고유 ID
  report_id BIGINT NOT NULL,             -- 조사 메인 보고서 ID
  area_id BIGINT,                        -- 조사 구역별 FK(선택)
  type VARCHAR(30) NOT NULL,
    CHECK (type IN ('INVESTIGATION_MAIN','INVESTIGATION_AREA','CLEANING_BEFORE_AREA','CLEANING_AFTER_AREA','COLLECTION_DEPOT','COLLECTION_COMPLETED')),
  file_name VARCHAR(255) NOT NULL,       -- S3 파일명
  ord INTEGER DEFAULT 0,                 -- 이미지 순서
  description VARCHAR(255),              -- 사진 설명(선택)
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (report_id) REFERENCES report(id),
  FOREIGN KEY (area_id) REFERENCES report_investigation_area(id)
);

-- 청소 보고서 이미지
CREATE TABLE report_cleaning_image (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 이미지 고유 ID
  report_id BIGINT NOT NULL,             -- 청소 메인 보고서 ID
  area_id BIGINT,                        -- 청소 구역별 FK(선택)
  type VARCHAR(30) NOT NULL,
    CHECK (type IN ('CLEANING_BEFORE_AREA','CLEANING_AFTER_AREA','COLLECTION_DEPOT','COLLECTION_COMPLETED')),
  file_name VARCHAR(255) NOT NULL,       -- S3 파일명
  ord INTEGER DEFAULT 0,                 -- 이미지 순서
  description VARCHAR(255),              -- 사진 설명(선택)
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (report_id) REFERENCES report(id),
  FOREIGN KEY (area_id) REFERENCES report_cleaning_area(id)
);

-- 알림
CREATE TABLE notification (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 알림 고유 ID
  user_id BIGINT,                        -- 수신자 FK
  type VARCHAR(10) NOT NULL,
    CHECK (type IN ('PUSH','SMS','EMAIL')),
  title VARCHAR(100),                    -- 제목
  message TEXT,                          -- 메시지 본문
  status VARCHAR(10) DEFAULT 'UNREAD',
    CHECK (status IN ('UNREAD','READ')),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 로그
CREATE TABLE log (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 로그 고유 ID
  user_id BIGINT,                        -- 관련 사용자 FK(선택)
  action VARCHAR(50) NOT NULL,           -- 이벤트/행동명
  detail TEXT,                           -- 상세 내용
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES user(id)
);

-- =========================
-- 주요 인덱스/성능/품질 정책
-- =========================
CREATE INDEX idx_user_phone ON user (phone);
CREATE INDEX idx_user_org_id ON user (org_id);
CREATE INDEX idx_report_type ON report (type);
CREATE INDEX idx_report_user_id ON report (user_id);
CREATE INDEX idx_report_team_id ON report (team_id);
CREATE INDEX idx_collection_depot_status ON collection_depot (status);
CREATE INDEX idx_notification_user_id ON notification (user_id);
CREATE INDEX idx_log_user_id ON log (user_id);

-- (추가 품질 정책, 샘플 데이터, 파티셔닝 등은 별도 마이그레이션 파일로 관리 권장) 