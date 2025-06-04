# 2-3 API 명세서 (Beach-Saver)

## 1. 설계 원칙 및 범위

- RESTful 경로/리소스 명명 일관성(공통 report 리소스, type 파라미터 활용)
- 역할/권한 기반(RBAC): PlatformAdmin, CityAdmin, DistrictAdmin, Investigator, Cleaner, Collector, Citizen 등 구체적 명시
- 실무 흐름/UX: 조사→청소→수거, 팀/이력/이미지/알림 등 실제 업무 기준
- 표준 응답/에러/페이징/정렬/필터: 모든 목록 API에 page, size, sort, filter 명확히
- 요청/응답 DTO: 모든 API별로 필드명, 타입, 필수/옵션, 제약조건, 설명 표기
- 오프라인/임시저장/동기화: 별도 API 및 플래그 지원
- 이미지 업로드: 기본은 별도 업로드 후 image_id 참조, base64 직접 포함은 예외적/권장X(모바일 특수 상황만 허용, 대용량/성능/보안 이슈 주의)
- collection_depot(임시 집하장)만 관리, collection_point(고정 집하장)는 미사용/미구현(필요시 별도 도입)
- 문서화/자동화: OpenAPI/Swagger/Postman 등 자동화 명세화 전제
- 보안/PII/감사: 인증(JWT), 민감정보 마스킹, 모든 주요 이벤트 로깅

> **정책/업무 흐름/용어/ID/관계/예외/엣지케이스/다이어그램/테이블 등은 반드시 [[README]] 및 [[1-1-프로젝트개요]]를 상위 정책으로 참조할 것.**

---

## 2. 공통 정책/파라미터/응답/에러

### 2.1. 표준 파라미터

| 파라미터 | 타입   | 필수 | 설명              | 예시                             |
| -------- | ------ | ---- | ----------------- | -------------------------------- |
| page     | int    | N    | 페이지 번호       | 1                                |
| size     | int    | N    | 페이지 크기       | 20                               |
| sort     | string | N    | 정렬 필드,방향    | createdAt,desc                   |
| filter   | string | N    | 필터(필드=값,...) | status=ACTIVE,type=investigation |

### 2.2. 표준 응답 구조

```json
{
  "success": true,
  "data": { ... },
  "error": null,
  "meta": { "page": 1, "size": 20, ... }
}
```

### 2.3. 표준 에러

| 필드    | 타입   | 설명                  |
| ------- | ------ | --------------------- |
| code    | string | 에러 코드             |
| message | string | 에러 메시지           |
| traceId | string | 트랜잭션/로그 추적 ID |

---

## 3. 인증/계정/권한

### 3.1. 인증/로그인

| 메서드 | 경로             | 설명                                                                           | 요청 DTO/파라미터 | 응답 DTO/예시 | 권한/비고 |
| ------ | ---------------- | ------------------------------------------------------------------------------ | ----------------- | ------------- | --------- |
| POST   | /api/auth/login  | 휴대폰+인증번호 로그인(인증번호는 **카카오톡 알림톡(기본), SMS(예외)**로 발송) | phone, code       | JWT, roles    | 전체      |
| POST   | /api/auth/verify | 인증번호 검증/JWT 발급                                                         | phone, code       | JWT, roles    | 전체      |
| POST   | /api/auth/logout | 세션/JWT 만료                                                                  | JWT               | -             | 전체      |

> ⚠️ **SSO(카카오/네이버/구글 등) 간편 로그인은 현재 미지원.**
>
> - 실무적 근거: 1) 핸드폰 인증(인증번호는 **카카오톡 알림톡(기본), SMS(예외)**로 발송)만으로도 전국 확장/공공기관/고령자/현장 작업자 UX에 최적화, 2) 1번호:N계정(역할 선택) 구조와 SSO(1:1 매핑) 혼합 시 계정/권한/운영/보안 복잡성↑, 3) SSO 연동/검수/개인정보 동의/탈퇴 등 외부 이슈, 4) 실제 현장/운영/보안/감사 기준에 부합하지 않음
> - SSO는 실제 필요/수요/운영 여건 발생 시에만 도입 검토(그때 정책/DB/UX/보안 등 전면 재설계)

#### 정책 요약

- 인증/로그인은 "휴대폰 번호 + 인증번호(인증번호는 **카카오톡 알림톡(기본), SMS(예외)**로 발송)" 방식만 지원
- 동일 번호로 여러 역할(조사자/청소자/수거자) 계정이 있으면 역할 선택 화면 제공 → 선택한 계정(ID)로 로그인
- SSO(카카오/네이버/구글 등)는 현재 미지원(향후 필요시 도입 가능)
- 전국 확장/공공기관/고령자/현장 작업자 UX/운영/보안/감사 기준에 최적화된 구조

### 3.2. 계정/프로필

| 메서드 | 경로                   | 설명                      | 요청 DTO/파라미터 | 응답 DTO/예시 | 권한/비고                               |
| ------ | ---------------------- | ------------------------- | ----------------- | ------------- | --------------------------------------- |
| GET    | /api/users/me          | 내 정보 조회              | JWT               | User DTO      | 전체                                    |
| PATCH  | /api/users/me          | 내 정보 수정              | nickname, phone   | User DTO      | 전체                                    |
| GET    | /api/users/roles       | 내 번호의 모든 역할/계정  | phone             | roles         | 전체                                    |
| POST   | /api/users/volunteer   | 시민 자가가입(임시계정)   | nickname, phone   | User DTO      | Citizen                                 |
| POST   | /api/users             | 관리자용 계정 생성        | UserCreate DTO    | User DTO      | PlatformAdmin, CityAdmin, DistrictAdmin |
| PATCH  | /api/users/{id}/status | 계정 상태 변경(정지/탈퇴) | status            | User DTO      | PlatformAdmin, CityAdmin, DistrictAdmin |

#### UserCreate DTO

| 필드명    | 타입   | 필수 | 설명         | 제약조건/예시  |
| --------- | ------ | ---- | ------------ | -------------- |
| system_id | string | N    | 시스템 ID    | 자동생성       |
| nickname  | string | Y    | 닉네임       | 2~20자         |
| phone     | string | Y    | 휴대폰 번호  | 01012345678    |
| password  | string | Y    | 비밀번호     | 8~30자, 암호화 |
| role      | string | Y    | 역할         | Enum           |
| org_id    | number | Y    | 소속 조직 ID |                |

#### User DTO

| 필드명     | 타입   | 설명         | 제약조건/예시 |
| ---------- | ------ | ------------ | ------------- |
| id         | number | 사용자 PK    |               |
| system_id  | string | 시스템 ID    |               |
| nickname   | string | 닉네임       |               |
| phone      | string | 휴대폰 번호  |               |
| role       | string | 역할         | Enum          |
| org_id     | number | 소속 조직 ID |               |
| status     | string | 상태         | Enum          |
| created_at | string | 생성일시     | ISO8601       |

---

## 4. 조직/팀/권한

| 메서드 | 경로                    | 설명                     | 요청 DTO/파라미터        | 응답 DTO/예시    | 권한/비고 |
| ------ | ----------------------- | ------------------------ | ------------------------ | ---------------- | --------- |
| GET    | /api/organizations      | 조직(시/구/군) 목록/검색 | page, size, sort, filter | OrganizationList | 전체      |
| GET    | /api/teams              | 팀 목록/검색             | org_id, page, ...        | TeamList         | 전체      |
| POST   | /api/teams              | 팀 생성                  | TeamCreate DTO           | Team DTO         | 관리자    |
| PATCH  | /api/teams/{id}         | 팀 정보/팀원 추가/변경   | TeamUpdate DTO           | Team DTO         | 관리자    |
| GET    | /api/teams/{id}/members | 팀원/이력/역할/기간 조회 | team_id                  | MemberList       | 전체      |

#### Organization DTO

| 필드명     | 타입   | 필수 | 설명         | 제약조건/예시 |
| ---------- | ------ | ---- | ------------ | ------------- |
| id         | number | Y    | 조직 PK      |               |
| name       | string | Y    | 조직명       |               |
| type       | string | Y    | 조직유형     | Enum          |
| parent_id  | number | N    | 상위 조직 ID |               |
| created_at | string | N    | 생성일시     | ISO8601       |

#### Team DTO

| 필드명     | 타입   | 필수 | 설명         | 제약조건/예시 |
| ---------- | ------ | ---- | ------------ | ------------- |
| id         | number | Y    | 팀 PK        |               |
| name       | string | Y    | 팀명         |               |
| org_id     | number | Y    | 소속 조직 ID |               |
| leader_id  | number | N    | 팀장 ID      |               |
| created_at | string | N    | 생성일시     | ISO8601       |

---

## 5. 해변/집하장

| 메서드 | 경로                                 | 설명                     | 요청 DTO/파라미터            | 응답 DTO/예시 | 권한/비고 |
| ------ | ------------------------------------ | ------------------------ | ---------------------------- | ------------- | --------- |
| GET    | /api/beaches                         | 해변 목록/검색           | org_id, ...                  | BeachList     | 전체      |
| POST   | /api/beaches                         | 해변 등록                | BeachCreate DTO              | Beach DTO     | 관리자    |
| GET    | /api/collection-depots               | 임시 집하장 목록/검색    | report_id, ...               | DepotList     | 전체      |
| POST   | /api/collection-depots               | 임시 집하장 등록(청소자) | DepotCreate DTO              | Depot DTO     | Cleaner   |
| PATCH  | /api/collection-depots/{id}/complete | 집하장 수거 완료 처리    | collected_amount, photo, ... | Depot DTO     | Collector |

> ⚠️ **collection_depot(임시 집하장)만 관리, collection_point(고정 집하장)는 미사용/미구현.**
>
> - collection_depot은 청소자가 청소 후 임의로 지정, 수거용 보고서(1:N) 하위 엔티티로만 관리
> - 수거자는 collection_depot 정보만 보고 수거 작업, 수거 완료 후 상태/사진만 기록
> - collection_depot 데이터는 수거 완료 후 장기 보관/통계/운영 불필요(필요시 soft delete/아카이빙)
> - **POST /api/collection-depots는 예외적/운영상 보완 용도로만 사용.**
>   - 주 흐름: report(type=collection_request) 생성 시 collection_depots 배열로 일괄 등록
>   - 예외: 이미 생성된 report에 누락 집하장 추가/수정/삭제 필요 시만 사용(운영상 보완)
>   - 가능하면 report 생성 시 한 번에 등록할 것(원자성/일관성/트랜잭션 보장)

#### Beach DTO

| 필드명     | 타입   | 필수 | 설명         | 제약조건/예시 |
| ---------- | ------ | ---- | ------------ | ------------- |
| id         | number | Y    | 해변 PK      |               |
| org_id     | number | Y    | 소속 조직 ID |               |
| beach_name | string | Y    | 해변명       |               |
| si         | string | Y    | 시           |               |
| gu_gun     | string | Y    | 구/군        |               |
| dong_eub   | string | Y    | 동/읍        |               |
| workplace  | string | Y    | 근무처       |               |
| latitude   | number | Y    | 위도         |               |
| longitude  | number | Y    | 경도         |               |
| created_at | string | N    | 생성일시     | ISO8601       |

#### Depot DTO

| 필드명                  | 타입   | 필수 | 설명           | 제약조건/예시 |
| ----------------------- | ------ | ---- | -------------- | ------------- |
| id                      | number | Y    | 집하장 PK      |               |
| report_id               | number | Y    | 수거용 보고서  |               |
| name                    | string | Y    | 집하장명       |               |
| address                 | string | N    | 집하장 주소    |               |
| latitude                | number | Y    | 위도           |               |
| longitude               | number | Y    | 경도           |               |
| status                  | string | Y    | 상태           | Enum          |
| collector_id            | number | N    | 수거자 ID      |               |
| photo_url               | string | N    | 집하장 사진    |               |
| collected_amount        | number | N    | 수거량(kg 등)  |               |
| collection_completed_at | string | N    | 수거 완료 시각 | ISO8601       |
| note                    | string | N    | 특이사항       |               |
| created_at              | string | N    | 생성일시       | ISO8601       |

#### DepotCreate DTO

| 필드명    | 타입   | 필수 | 설명          | 제약조건/예시        |
| --------- | ------ | ---- | ------------- | -------------------- |
| report_id | number | Y    | 수거용 보고서 |                      |
| name      | string | Y    | 집하장명      |                      |
| address   | string | N    | 집하장 주소   |                      |
| latitude  | number | Y    | 위도          |                      |
| longitude | number | Y    | 경도          |                      |
| photo     | file   | N    | 집하장 사진   | image_id/base64 구분 |
| note      | string | N    | 특이사항      |                      |

---

## 6. 보고서(공통 report 리소스)

### 6.1. 보고서 통합 API

| 메서드 | 경로              | 설명                 | 요청 DTO/파라미터 | 응답 DTO/예시 | 권한/비고 |
| ------ | ----------------- | -------------------- | ----------------- | ------------- | --------- |
| GET    | /api/reports      | 보고서 목록/검색     | type, page, ...   | ReportList    | 권한별    |
| POST   | /api/reports      | 보고서 생성          | ReportCreate DTO  | Report DTO    | 권한별    |
| GET    | /api/reports/{id} | 보고서 상세          | report_id         | Report DTO    | 권한별    |
| PATCH  | /api/reports/{id} | 보고서 수정/상태변경 | ReportUpdate DTO  | Report DTO    | 권한별    |

#### ReportCreate DTO (통합)

| 필드명            | 타입    | 필수 | 설명                         | 제약조건/예시                                                          |
| ----------------- | ------- | ---- | ---------------------------- | ---------------------------------------------------------------------- |
| type              | string  | Y    | 보고서 유형                  | investigation/cleaning/collection_request/collection_completed         |
| beach_id          | number  | Y    | 해변 ID                      |                                                                        |
| investigation_at  | string  | N    | 조사 일시                    | ISO8601                                                                |
| cleaning_at       | string  | N    | 청소 일시                    | ISO8601                                                                |
| team_id           | number  | N    | 팀 ID                        |                                                                        |
| areas             | array   | N    | 구역 리스트                  | [{...}]                                                                |
| collection_depots | array   | N    | 집하장 리스트(수거용 보고서) | [{ name, address, latitude, longitude, photo(image_id/base64), note }] |
| images            | array   | N    | 이미지 ID 리스트             | [1001, 1002] (image_id 참조, base64는 예외적)                          |
| is_draft          | boolean | N    | 임시저장 여부                |                                                                        |
| note              | string  | N    | 특이사항                     |                                                                        |

#### Report DTO (공통)

| 필드명     | 타입   | 설명        | 제약조건/예시 |
| ---------- | ------ | ----------- | ------------- |
| id         | number | 보고서 PK   |               |
| type       | string | 보고서 유형 | Enum          |
| status     | string | 상태        | Enum          |
| user_id    | number | 작성자 ID   |               |
| team_id    | number | 팀 ID       |               |
| beach_id   | number | 해변 ID     |               |
| created_at | string | 생성일시    | ISO8601       |
| updated_at | string | 수정일시    | ISO8601       |
| ...        | ...    | ...         | ...           |

#### ReportUpdate DTO (공통)

| 필드명 | 타입   | 필수 | 설명     | 제약조건/예시 |
| ------ | ------ | ---- | -------- | ------------- |
| status | string | N    | 상태     | Enum          |
| note   | string | N    | 특이사항 |               |
| ...    | ...    | ...  | ...      | ...           |

---

## 7. 이미지 업로드/관리

| 메서드 | 경로             | 설명                 | 요청 DTO/파라미터    | 응답 DTO/예시 | 권한/비고 |
| ------ | ---------------- | -------------------- | -------------------- | ------------- | --------- |
| POST   | /api/images      | 이미지 업로드        | file, type, (base64) | image_id      | 전체      |
| GET    | /api/images/{id} | 이미지 메타/다운로드 | image_id             | Image DTO     | 전체      |

#### 이미지 업로드 정책

- **기본:** 이미지 별도 업로드 후 반환된 image_id를 보고서/집하장 등에서 참조
- **옵션:** base64 직접 포함도 지원(단, 대용량/성능/보안 이슈로 예외적, 모바일 특수 상황만 허용)
- **DTO:** image_id, base64 등 필드 구분 명확히 표기

#### Image DTO

| 필드명     | 타입   | 설명        | 제약조건/예시 |
| ---------- | ------ | ----------- | ------------- |
| id         | number | 이미지 PK   |               |
| type       | string | 이미지 유형 | Enum          |
| file_url   | string | 이미지 URL  |               |
| created_at | string | 생성일시    | ISO8601       |

---

## 8. 알림/로그/통계

| 메서드 | 경로                    | 설명                         | 요청 DTO/파라미터 | 응답 DTO/예시    | 권한/비고 |
| ------ | ----------------------- | ---------------------------- | ----------------- | ---------------- | --------- |
| GET    | /api/notifications      | 내 알림 목록/읽음처리/상세   | page, ...         | NotificationList | 전체      |
| POST   | /api/notifications/read | 알림 읽음 처리(일괄/개별)    | ids               | -                | 전체      |
| GET    | /api/logs               | 내/조직/시스템 로그          | user_id, ...      | LogList          | 권한별    |
| GET    | /api/statistics         | 실적/통계(조직/팀/개인/기간) | org_id, ...       | StatList         | 권한별    |

#### Notification DTO

| 필드명     | 타입   | 설명        | 제약조건/예시 |
| ---------- | ------ | ----------- | ------------- |
| id         | number | 알림 PK     |               |
| user_id    | number | 수신자 ID   |               |
| type       | string | 알림 유형   | Enum          |
| title      | string | 제목        |               |
| message    | string | 메시지 본문 |               |
| status     | string | 읽음/안읽음 | Enum          |
| created_at | string | 생성일시    | ISO8601       |

---

## 9. 오프라인/임시저장/동기화

| 메서드 | 경로               | 설명                   | 요청 DTO/파라미터 | 응답 DTO/예시 | 권한/비고 |
| ------ | ------------------ | ---------------------- | ----------------- | ------------- | --------- |
| POST   | /api/reports/draft | 보고서 임시저장        | ReportCreate DTO  | Report DTO    | 권한별    |
| POST   | /api/reports/sync  | 오프라인 보고서 동기화 | ReportSync DTO    | SyncResult    | 권한별    |

#### ReportSync DTO

| 필드명  | 타입  | 설명        | 제약조건/예시      |
| ------- | ----- | ----------- | ------------------ |
| reports | array | 동기화 대상 | [ReportCreate DTO] |

---

## 10. 예시 시퀀스/업무 흐름과 API 매핑

1. **조사자**:
   - POST /api/reports (type=investigation, ...)
2. **청소팀장**:
   - POST /api/reports (type=cleaning, investigation_report_id=...)
3. **청소자**:
   - POST /api/reports (type=collection_request, collection_depots=[...])
   - POST /api/collection-depots (임시 집하장 등록, 필요시/예외적)
4. **수거자**:
   - PATCH /api/collection-depots/{id}/complete (집하장별 수거 완료)
   - POST /api/reports (type=collection_completed, ...)

> ⚠️ 집하장은 임시(청소자가 지정), 수거 완료 후 별도 관리/통계/운영 불필요. 고정 집하장(행정상 공식 장소)은 미사용.
>
> - 임시 집하장 등록/수정/삭제는 report 생성 시 일괄 처리 권장, 예외적 상황만 개별 API 사용

---

## 11. 권한/역할/데이터 범위 표

| 역할          | API 접근/수정 범위         | 설명/제약조건                              |
| ------------- | -------------------------- | ------------------------------------------ |
| PlatformAdmin | 전체 데이터/계정/조직/로그 | 시스템 전체 관리 권한                      |
| CityAdmin     | 본인 시청 이하             | 시 단위 관리/운영                          |
| DistrictAdmin | 본인 구청 이하             | 구/군 단위 관리/운영                       |
| Investigator  | 본인 데이터만              | 조사 보고서 작성/조회                      |
| Cleaner       | 본인/팀 데이터만           | 청소/수거용 보고서 작성/조회               |
| Collector     | 본인 데이터만              | 집하장 수거 완료 처리/수거완료 보고서 작성 |
| Citizen       | 본인 데이터(승인 전 제한)  | 자원봉사자, 승인 전 제한적 기능            |

---

## 12. 엣지케이스/운영 팁

- 모든 주요 API는 로깅/감사/이력 필수
- 대용량 이미지/보고서/알림 등은 비동기/배치/파티셔닝 고려
- 실무적 확장(전국/다도시/다조직/다권한) 구조 내재화
- collection_point(고정 집하장) vs collection_depot(수거용 집하장) 혼동 주의(현 정책상 collection_point 미사용)
- 오프라인/임시저장/동기화 정책 명확히
- 임시 집하장 등록/수정/삭제는 report 생성 시 일괄 처리 권장, 예외적 상황만 개별 API 사용
- 이미지 업로드는 image_id 참조 방식이 기본, base64 직접 업로드는 예외적(권장X)
- DTO/응답 구조는 OpenAPI/Swagger 자동화 명세와 1:1 매핑 가능하게 설계

---

## 13. 상세 예시 (주요 API)

### 1. 보고서 생성 (POST /api/reports)

#### 요청 DTO

```json
{
  "type": "investigation",
  "beach_id": 1,
  "investigation_at": "2024-06-01T10:00:00",
  "areas": [
    {
      "area_name": "A구역",
      "start_latitude": 35.123,
      "start_longitude": 129.123,
      "end_latitude": 35.124,
      "end_longitude": 129.124,
      "trash_amount_est": 100,
      "main_trash_type": "플라스틱",
      "note": "특이사항 없음"
    }
  ],
  "images": [1001, 1002],
  "is_draft": false
}
```

#### 응답 DTO

```json
{
  "success": true,
  "data": { "report_id": 123 },
  "error": null
}
```

### 2. 집하장 수거 완료 처리 (PATCH /api/collection-depots/{id}/complete)

#### 요청 DTO

```json
{
  "collected_amount": 50,
  "collector_id": 20,
  "collection_completed_at": "2024-06-03T11:00:00",
  "photo": 2001
}
```

#### 응답 DTO

```json
{
  "success": true,
  "data": { "collection_depot_id": 1 },
  "error": null
}
```

---

## 14. 참고/운영 문서

- [DB설계: docs/2-설계/2-2-DB설계.md]
- [시스템 아키텍처: docs/2-설계/2-1-시스템아키텍처.md]
- [정책/흐름/엣지케이스: docs/README.md]
