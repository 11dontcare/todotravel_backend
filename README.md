# 🧳 **TodoTravel** - 여행 계획 공유 플랫폼 (https://todotravel.kro.kr/)

- 프로젝트 기간 : 7월 23일 (화) ~ 9월 6일 (금)
![image](https://github.com/user-attachments/assets/3448f6ca-5965-466d-85c1-401d42cc84b8)

    

## 👨‍👩‍👧‍👦 **팀 소개 (Team)**

<br>

|이름|역할|깃허브|
|------|---|---|
|전준영(팀장)|사용자 인증, 메인 페이지, 채팅, CI/CD + 배포|https://github.com/patric7732|
|조은상|사용자 인증, 마이페이지, 플랜 페이징, CI/CD + 배포|https://github.com/honeyWater|
|김민정|여행 플랜 & 모집 플랜 & 여행 플랜 참여자 관리|https://github.com/minjung415|
|정유진|여행 일정, 여행 일정 투표리스트, 알림|https://github.com/jini814|
|양승경|채팅|https://github.com/ysgyeong00|

<br>

## **🌁 프로젝트 개요 (Overview)**
TodoTravle은 사용자가 전반적인 일정을 세우고 그 일정을 모집 게시판에 올려 첫 시작부터 같이 여행 갈 인원이나 중간에 합류해서 여행을 즐길 인원, 또는 번개로 해당 장소에만 같이 어울려 다닐 인원을 모집할 수 있는 기능을 가진 플랫폼입니다.

- 함께 여행 계획을 세울 때 소통하는 과정과 방대한 여행 정보들로 인해 우리는 많은 어려움과 불편함을 마주합니다. 이 플랫폼은 사용자들이 보다 쉽고 효율적으로 여행 계획을 짤 수 있는 플랫폼을 생각했습니다.
**→ 함께 여행 계획 세우기**

- 자신의 여행 경험을 기록하고 상세한 여행 계획을 공유할 곳이 부족하다고 느꼈고 보다 쉽고 체계적으로 여행 정보를 관리할 수 있도록 하는 플랫폼을 생각했습니다.
**→ 자신만의 여행 계획 공유하기**

 

- 일정을 작성하고 동행할 수 있는 여행자를 찾을 때 일반적으로 행하는 타 커뮤니티에 글을 작성해 찾는 방식이 복잡하다고 느껴졌습니다. 그래서 사용자가 전반적인 일정을 세우고 그 일정을 모집 게시판에 올려 일정과 인원 모집에 대해 한번에 할 수 있는 플랫폼을 생각했습니다.
**→ 같이 여행할 사람 모집하기**

  
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

“/src/main/resources/application.yml” 경로로 yml 파일을 추가

- application.yml
    - `/src/main/resources/application.yml`
        
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
        
<br>

## 화면 구성

| 이미지 | 기능 |
|---|-----|
| ![image](https://github.com/user-attachments/assets/322bc26d-0d2b-488a-b764-4908fd3670ed) | 메인 홈페이지 화면 |
| ![image](https://github.com/user-attachments/assets/d8c1deec-2e30-4e74-9f9c-8f5560da745c) | 메인 홈페이지 화면2 |
| ![image](https://github.com/user-attachments/assets/28227ce0-592a-4dee-9d8d-b32bf6b1980c) | 채팅 |
| ![image](https://github.com/user-attachments/assets/26a61de0-d8b0-46be-8820-1ec8578856e8) | 마이페이지 |
| ![image](https://github.com/user-attachments/assets/c1a4b978-bb42-4a1c-bdc9-6b3632f57319) | 플랜 만들기 |
| ![image](https://github.com/user-attachments/assets/74dcf701-df69-4988-8708-986b995501e7) | 플랜 함께하기 리스트 |
| ![image](https://github.com/user-attachments/assets/fb91c668-0462-4375-93f0-98d58ba6024f) | 모집 함께하기 리스트 |
| ![image](https://github.com/user-attachments/assets/0294158c-ee1c-4d88-972b-beba0f9afc91) | 플랜 상세보기 |




<br>

## 🌟 **주요 기능 (Features)**

## BackEnd

### ❤️‍🔥 **사용자 인증**

- OAuth를 이용한 GOOGLE, KAKAO, NAVER  로그인 및 일반 회원가입(이메일 인증) 후 JWT을 통한 인증/인가
- 일반 회원가입 사용자, OAuth 로그인 사용자에 따라서 다른 정보 표출

### ❤️‍🔥 **여행 플랜**

- 플랜 생성 & 수정 & 삭제
- 제목, 여행지, 예산안 등 여행 정보 입력
- 모든 참가자가 수정 가능
- 삭제, 타사용자 초대는 생성자만 가능

### ❤️‍🔥 일정

- 일정 생성 & 수정 & 삭제
- 위치를 검색하여 경도, 위도, 이름으로 추가
- 일정에 대한 예산, 이동수단, 메모 입력
- 진행중인 플랜에 대해서 모든 참가자가 수정 가능
- 투표리스트에 장소 추가 & 수정 & 삭제
- 카테고리 별로 분류하여 중복 투표 가능

### ❤️‍🔥 모집

- 함께 여행할 사용자 모집 기능
- 글의 생성자는 플랜 글을 모집중, 취소 상태로 변경
- 타 사용자들은 모집 중인 플랜에 참가 요청

### ❤️‍🔥 **마이 페이지**

- 사용자가 자신의 프로필 및 개인정보 확인 가능
- 타 사용자 팔로우/언팔로우 가능
- 사용자가 참여하거나 생성한 플랜, 모집 중인 플랜, 좋아요나 북마크한 플랜, 댓글단 플랜에 대해서 접근 및 관리 가능
- 회원탈퇴

### ❤️‍🔥 **알림**

- 자동으로 서버에서 이벤트 발행
- 사용자 초대 및 거절 알림
- 플랜 글에 대한 좋아요, 북마크, 댓글 알림

### ❤️‍🔥 **채팅**

- 개인, 그룹 간의 채팅 기능

## FrontEnd

### ❤️‍🔥 ProtectedRoute

- 로그인한 사용자만 서비스 이용이 가능하도록 구분함

### ❤️‍🔥 KaKaoMap API

- 카카오맵 API를 활용하여 위치 검색
- 카카오맵 API를 활용하여 위치 표출

### ❤️‍🔥 실시간채팅

- Stomp/websocket을 사용하여 실시간 채팅 기능 구현
- WebSocket 연결을 통해 사용자 간 메시지 주고받으며, 새로운 메시지가 즉시 갱신
- 채팅 기록이 자동으로 저장 및 관리

### ❤️‍🔥 무한스크롤

- 사용자가 페이지를 아래로 스크롤할 때 새로운 데이터가 자동으로 로드
- 사용자가 더 많은 콘텐츠를 볼 수 있도록 페이지 갱신없이 추가 로드
- 페이지네이션을 대체하여 사용자 경험을 향상
  
<br>

## **🔨** **프로젝트 구조 (Project Structure)**

<br>

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
