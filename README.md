# 📚 책읽아웃 프로젝트

> 서로가 읽은 책을 나누며, 내가 몰랐던 좋은 책들을 하나씩 알아가는 책 리뷰 플랫폼입니다.  
> 인기 리뷰, 인기 검색어, 조회 기반 맞춤 추천 등 다양한 기능을 제공합니다.

## 📌 목차
- [주요 기능](#주요-기능)
- [ERD](#erd)
- [API 문서](#api-문서)
- [팀원 역할](#팀원-역할)
- [기술 스택](#기술-스택)

---

## 🚀 주요 기능

- ✅ JWT 기반 로그인 및 회원가입
- ✅ 책 등록 / 수정 / 삭제
- ✅ 리뷰 작성 / 조회 / 삭제
- ✅ QueryDSL 기반 책 검색
- ✅ 인기 검색어 기능 (Redis 활용)
- ✅ 조회 기록 기반 맞춤 추천 기능

---

## 🧩 ERD

책읽아웃 프로젝트의 데이터베이스 구조는 다음과 같습니다:

![ERD](https://img.notionusercontent.com/s3/prod-files-secure%2F83c75a39-3aba-4ba4-a792-7aefe4b07895%2F9b08add2-6941-4705-a3a8-c06f176bc090%2Fimage.png/size/w=1420?exp=1748161929&sig=tukoDT2PkQ8jnsZ6za-7kUqDHCMSgyFLu4S57LsH9GI&id=1f82dc3e-f514-8040-8151-c95de5c46587&table=block)

---

## 📄 API 문서

- Postman 문서: 👉 [API 문서 바로가기](https://documenter.getpostman.com/view/43186270/2sB2qcB11y)

---

## 👥 팀원 역할

| 이름     | 담당 기능                         |
|----------|----------------------------------|
| 김정연   | 사용자 인증 및 회원 관리 (`User`, `Auth`) |
| 임민지   | 리뷰 기능 전반 (`Review`)         |
| 김민우   | 도서 등록 및 관리 기능 (`Book`)   |
| 정승원   | 검색 기능 및 관리자 관련 API (`Search`, `Admin`) |
| 박소윤   | 좋아요 기능 (`Like`)              |

---

## 🛠 기술 스택

| 구분       | 사용 기술                         |
|------------|----------------------------------|
| 언어       | Java 17                          |
| 백엔드     | Spring Boot 3, Spring Security   |
| 인증       | JWT                               |
| 데이터베이스 | MySQL                           |
| ORM        | JPA, QueryDSL
