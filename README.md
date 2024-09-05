# 🧳 **TodoTravel** - 여행 계획 공유 플랫폼

[**TodoTravel 사이트 바로가기**](https://todotravel.kro.kr/)

---

## 👨‍👩‍👧‍👦 **팀 소개 (Team)**

| 이름           | 역할                                           | 깃허브                                             |
| -------------- | ---------------------------------------------- | -------------------------------------------------- |
| **전준영 (팀장)** | 사용자 인증, 메인 페이지, 채팅, CI/CD + 배포     | [GitHub](https://github.com/patric7732)             |
| **조은상**     | 사용자 인증, 마이페이지, 플랜 페이징, CI/CD + 배포 | [GitHub](https://github.com/honeyWater)             |
| **김민정**     | 여행 플랜 & 모집 플랜 & 여행 플랜 참여자 관리     | [GitHub](https://github.com/minjung415)             |
| **정유진**     | 여행 일정, 여행 일정 투표리스트, 알림            | [GitHub](https://github.com/jini814)                |
| **양승경**     | 채팅                                             | [GitHub](https://github.com/ysgyeong00)             |

---

## 🌁 **프로젝트 개요 (Overview)**

**TodoTravel**은 여행 계획을 함께 세우고, 여행 동행자를 모집할 수 있는 플랫폼입니다. 사용자는 일정 초반부터 함께 여행할 인원을 모집하거나, 중간에 합류할 사람을 구할 수 있으며, 특정 장소에서만 어울릴 사람을 번개로 찾을 수도 있습니다.

### 프로젝트 목표
1. **함께 여행 계획 세우기**  
   여행 계획을 세울 때 소통의 불편함과 방대한 정보로 인해 혼란을 겪을 수 있습니다. TodoTravel은 사용자가 더 쉽게 여행 일정을 관리하고 소통할 수 있는 기능을 제공합니다.

2. **자신만의 여행 계획 공유하기**  
   여행 경험을 기록하고 공유할 수 있는 체계적인 플랫폼이 부족하다는 점을 해결합니다. 사용자는 자신만의 여행 일정을 쉽게 관리하고 다른 사용자와 공유할 수 있습니다.

3. **같이 여행할 사람 모집하기**  
   여행 동행자를 찾는 과정을 간편하게 해결할 수 있습니다. 기존의 복잡한 커뮤니티 방식 대신, TodoTravel에서는 한 번에 여행 일정과 동행자를 모집할 수 있습니다.

---

  
<br>

## **🔧** **기술 스택 (Tech Stack)**
### Environment

<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"> <img src="https://img.shields.io/badge/intellijidea-000000?style=for-the-badge&logo=intellijidea&logoColor=white"> <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"> <img src="https://img.shields.io/badge/npm-CB3837?style=for-the-badge&logo=npm&logoColor=white">


### Development
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"> <img src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=black">   <img src="https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white"> <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> 

### Database
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/mongoDB-47A248?style=for-the-badge&logo=MongoDB&logoColor=white"> 

### Server
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white"/>   <img src="https://img.shields.io/badge/amazonaws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white"> <img src="https://img.shields.io/badge/nginx-009639?style=for-the-badge&logo=nginx&logoColor=white">

### Collaboration Tools
<img src="https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=Figma&logoColor=white"/> <img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white"/> <img src="https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=Discord&logoColor=white"/> <img src="https://img.shields.io/badge/Kakao-FFCD00?style=for-the-badge&logo=Kakao&logoColor=white"/> 

<br>

## ⌨️ **설치 및 실행 방법 (Installation & Setup)**

## Requirements

설치에 있어서 필요한 것

- Node.js v20.11.1, Npm v10.8.3
- spring boot 3.3.2 + jdk 21

## Installation

```bash
# FrontEnd
$ git clone https://github.com/11dontcare/todotravel_frontend.git

# BackEnd
$ git clone https://github.com/11dontcare/todotravel_backend
```

### FrontEnd

```bash
$ npm install
$ npm start
```

### BackEnd

![image](https://github.com/user-attachments/assets/16111b95-b93a-4aae-8054-d110a2e78674)

## application.yml

<details>
  <summary>클릭하여 application.yml 파일 보기</summary>

  ```yaml
  spring:
    application:
      name: todotravel
  
    data:
      mongodb:
        uri: ${MONGODB_URI}
  
    datasource:
      url: ${MYSQL_URL}
      username: ${MYSQL_USERNAME}
      password: ${MYSQL_PASSWORD}
      driver-class-name: com.mysql.cj.jdbc.Driver
  
    jpa:
      hibernate:
        ddl-auto: update
      show-sql: true
      properties:
        hibernate:
          dialect: org.hibernate.dialect.MySQLDialect
  
    mail:
      host: smtp.gmail.com  # SMTP 서버 호스트
      port: 587 # SMTP 서버 포트
      username: ${SMTP_MAIL_USERNAME}  # SMTP 서버 로그인 아이디
      password: ${SMTP_MAIL_PASSWORD}  # SMTP 서버 로그인 패스워드 : 앱 비밀번호
      properties:
        mail:
          smtp:
            auth: true  # 사용자 인증 시도 여부 (기본값 : false)
            timeout: 5000 # Socket Read Timeout 시간(ms)(기본값 : 무한대)
            starttls:
              enable: true # StartTLS 활성화 여부 (기본값 : false)
  
    # oauth2 에 대한 설정
    security:
      oauth2:
        client:
          registration:
            google:
              client-id: ${GOOGLE_CLIENT_ID}
              client-secret: ${GOOGLE_CLIENT_SECRET}
              redirect-uri: http://localhost:8080/oauth2/callback/google
              scope: profile, email
  
            kakao:
              client-id: ${KAKAO_CLIENT_ID}
              client-secret: ${KAKAO_CLIENT_SECRET}
              scope: profile_nickname, account_email
              authorization-grant-type: authorization_code
              redirect-uri: http://localhost:8080/oauth2/callback/kakao
              client-name: Kakao
              client-authentication-method: client_secret_post
  
            naver:
              client-id: ${NAVER_CLIENT_ID}
              client-secret: ${NAVER_CLIENT_SECRET}
              scope: name, email, nickname
              client-name: Naver
              authorization-grant-type: authorization_code
              redirect-uri: http://localhost:8080/oauth2/callback/naver
  
          provider:
            kakao:
              authorization-uri: https://kauth.kakao.com/oauth/authorize
              token-uri: https://kauth.kakao.com/oauth/token
              user-info-uri: https://kapi.kakao.com/v2/user/me
              user-name-attribute: id
  
            naver:
              authorization-uri: https://nid.naver.com/oauth2.0/authorize
              token-uri: https://nid.naver.com/oauth2.0/token
              user-info-uri: https://openapi.naver.com/v1/nid/me
              user-name-attribute: response
  
  jwt:
    secretKey: ${JWT_SECRET_KEY}
    refreshKey: ${JWT_REFRESH_KEY}
  
  cloud:
    aws:
      credentials:
        accessKey: ${AWS_CREDENTIALS_ACCESS_KEY}
        secretKey: ${AWS_CREDENTIALS_SECRET_KEY}
      s3:
        bucket: ${S3_BUCKET_NAME}
      stack:
        auto: false
      region:
        static: ${AWS_REGION}
  
  server:
    port: 8080
  
  app:
    cors:
      allowed-origins: http://localhost:3000
```
</details>
<br>

## 🌟 **주요 기능 (Features)**

### 🔥 BackEnd

#### ❤️‍🔥 **사용자 인증**

- **OAuth 로그인**: Google, Kakao, Naver를 이용한 소셜 로그인 지원
- **일반 회원가입**: 이메일 인증 후 JWT를 통한 인증/인가 처리
- 로그인 방식에 따라 다른 사용자 정보 표시

#### ❤️‍🔥 **여행 플랜**

- **플랜 관리**: 플랜 생성, 수정, 삭제 가능
- 여행 정보 입력: 제목, 여행지, 예산안 등
- **참가자 수정 가능**: 모든 참가자가 플랜 수정 가능 (생성자만 삭제 및 초대 권한 보유)

#### ❤️‍🔥 **일정 관리**

- 일정 생성, 수정, 삭제 기능
- **위치 검색**: 경도, 위도, 이름 기반으로 일정 추가
- **일정 정보**: 예산, 이동수단, 메모 입력 가능
- **참가자 수정 가능**: 모든 참가자가 진행 중인 플랜 일정 수정 가능
- **투표 리스트**: 장소 추가, 수정, 삭제 가능 (중복 투표 가능)

#### ❤️‍🔥 **모집**

- 여행 동행자 모집 기능
- 플랜 생성자는 모집 상태 변경 가능 (모집 중, 취소)
- 타 사용자는 모집 중인 플랜에 참가 요청 가능

#### ❤️‍🔥 **마이 페이지**

- **프로필 및 개인정보 확인**: 사용자의 정보 관리
- **팔로우/언팔로우**: 타 사용자와의 소셜 연결
- **플랜 관리**: 참여/생성한 플랜, 모집 중인 플랜, 좋아요/북마크/댓글 단 플랜 접근 및 관리
- **회원탈퇴** 기능 지원

#### ❤️‍🔥 **알림 시스템**

- 서버 이벤트 기반 자동 알림 발행
- **사용자 초대 및 거절 알림**
- **플랜 관련 알림**: 좋아요, 북마크, 댓글 알림

#### ❤️‍🔥 **채팅 기능**

- **개인/그룹 채팅** 지원

---

### 🔥 FrontEnd

#### ❤️‍🔥 **ProtectedRoute**

- 로그인한 사용자만 서비스 이용 가능하도록 구분

#### ❤️‍🔥 **KaKaoMap API**

- **위치 검색**: 카카오맵 API를 사용하여 위치 검색 및 표시

#### ❤️‍🔥 **실시간 채팅**

- **Stomp/WebSocket** 기반 실시간 채팅 구현
- **즉시 갱신**: 새로운 메시지가 실시간으로 업데이트
- **채팅 기록 저장**: 자동으로 채팅 기록 관리

#### ❤️‍🔥 **무한 스크롤**

- **자동 로드**: 사용자가 페이지를 스크롤하면 자동으로 새로운 데이터 로드
- 페이지네이션을 대체하여 사용자 경험을 향상


  
<br>

## **🔨** **프로젝트 구조 (Project Structure)**

## BackEnd

- structure
    
    ```
    └─todotravel
        ├─domain
        │  ├─chat
        │  │  ├─controller
        │  │  ├─dto
        │  │  │  ├─request
        │  │  │  └─response
        │  │  ├─entity
        │  │  ├─repository
        │  │  └─service
        │  │      └─impl
        │  ├─notification
        │  │  ├─controller
        │  │  ├─dto
        │  │  │  ├─request
        │  │  │  └─response
        │  │  ├─entity
        │  │  ├─repository
        │  │  └─service
        │  │      └─implement
        │  ├─plan
        │  │  ├─controller
        │  │  ├─dto
        │  │  │  ├─request
        │  │  │  └─response
        │  │  ├─entity
        │  │  ├─repository
        │  │  └─service
        │  │      └─implement
        │  └─user
        │      ├─controller
        │      ├─dto
        │      │  ├─request
        │      │  └─response
        │      ├─entity
        │      ├─repository
        │      └─service
        │          └─impl
        └─global
            ├─aws
            ├─config
            │  ├─aws
            │  ├─jpa
            │  ├─mongo
            │  ├─security
            │  └─socket
            ├─controller
            ├─dto
            ├─exception
            ├─handler
            ├─jwt
            │  ├─filter
            │  ├─token
            │  └─util
            ├─oauth2
            │  ├─handler
            │  ├─service
            │  └─userinfo
            └─security
    ```
    

## **FrontEnd**

- structure
    
    ```jsx
    todotravel
    ├── ProtectedRoute.js
    ├── component
    │   ├── Fragment
    │   ├── Layout
    │   ├── Notification
    │   ├── auth
    │   ├── chat
    │   ├── mypage
    │   └── plan
    │       ├── Schedule
    │       └── Vote
    ├── constant
    ├── context
    └── service
    ```
    
<br>

## 💽 ERD
![image](https://github.com/user-attachments/assets/e612cafc-7c5f-4090-96d6-d92bcf2657b4)

<br>

## ✍️ 컨벤션 (Convention)

<br>

- Git Commit Message Convention
    - 커밋 유형은 영어 대문자로 작성하기
    - 제목과 본문을 빈행으로 분리
    - 제목 첫 글자는 대문자로, 끝에는 . 금지
    - 제목은 영문 기준 50자 이내로 할 것
    - 자신의 코드가 직관적으로 바로 파악할 수 있다고 생각하지 말자
    - 여러가지 항목이 있다면 글머리 기호를 통해 가독성 높이기
    
<br>

## ⚠️ 팀원 간 의사소통 및 문제 해결

- 디스코드 서버를 따로 설정하여 서로 간의 문제를 확인하고 해결
- 필요한 내용을 각 디스코드 채널에 메시지나 실시간으로 음성 채널 + 화면 공유를 통해 해결
