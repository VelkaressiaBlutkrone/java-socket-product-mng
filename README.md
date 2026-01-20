# Java Socket Product Management System

## 📋 설명 (Description)
Java Socket을 이용한 클라이언트-서버 기반 **상품 관리 시스템**입니다. 
Socket 통신을 통해 클라이언트에서 요청한 상품 관련 작업(조회, 추가, 삭제)을 서버에서 처리하고, 
MySQL 데이터베이스에 저장된 상품 정보를 관리합니다.

### 주요 기능
- **목록조회**: 전체 상품 목록 조회
- **상세조회**: 특정 상품 ID로 상세 정보 조회
- **상품추가**: 새로운 상품 등록
- **상품삭제**: 기존 상품 삭제

---

## 🏗️ 구성 (Project Structure)

java-socket-product-mng/
├── src/main/java/
│   ├── client/
│   │   ├── MyClient.java          # 클라이언트 메인 클래스
│   │   └── ClientService.java     # 클라이언트 서비스 (요청/응답 처리)
│   ├── server/
│   │   ├── MyServer.java          # 서버 메인 클래스
│   │   ├── Product.java           # 상품 엔티티
│   │   ├── ProductService.java    # 상품 비즈니스 로직
│   │   ├── ProductServiceInterface.java  # 서비스 인터페이스
│   │   ├── ProductRepository.java # 데이터베이스 접근 계층
│   │   └── DbConnection.java      # 데이터베이스 연결
│   └── dto/
│       ├── RequestDto.java        # 클라이언트 요청 DTO
│       └── ResponseDto.java       # 서버 응답 DTO
├── build.gradle                   # Gradle 의존성 설정
└── settings.gradle                # Gradle 설정

### 사용 기술 & 라이브러리
- **언어**: Java
- **빌드 도구**: Gradle
- **데이터베이스**: MySQL
- **Socket 통신**: Java Socket API
- **JSON 처리**: GSON
- **로깅**: SLF4J, Logback
- **유틸리티**: Lombok
- **드라이버**: MySQL JDBC Connector

---

## 🚀 실행방법 (How to Run)

### 필수 사전 요구사항
1. **Java JDK** (11 이상)
2. **MySQL** 설치 및 실행
3. **Gradle** (선택: gradlew 사용 가능)

### 1단계: 데이터베이스 설정

MySQL에 접속하여 다음 명령어 실행:

-- 데이터베이스 생성
CREATE DATABASE productdb;

-- 데이터베이스 선택
USE productdb;

-- 상품 테이블 생성
CREATE TABLE product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price INT NOT NULL,
    qty INT NOT NULL
);

-- 초기 샘플 데이터 삽입 (선택)
INSERT INTO product (name, price, qty) VALUES ('상품1', 10000, 5);
INSERT INTO product (name, price, qty) VALUES ('상품2', 20000, 3);

### 2단계: 데이터베이스 연결 설정

src/main/java/server/DbConnection.java 파일에서 데이터베이스 정보 수정:

String url = "jdbc:mysql://localhost:3306/productdb";
String username = "root";          // 본인의 MySQL 사용자명
String password = "bitc5600!";     // 본인의 MySQL 비밀번호

### 3단계: 서버 실행

터미널 1에서 서버 시작:

./gradlew build
java -cp build/libs/java-socket-product-mng-1.0-SNAPSHOT.jar server.MyServer

또는 IDE(IntelliJ, Eclipse 등)에서 MyServer 클래스를 직접 실행

### 4단계: 클라이언트 실행

터미널 2에서 클라이언트 시작:

java -cp build/libs/java-socket-product-mng-1.0-SNAPSHOT.jar client.MyClient

또는 IDE에서 MyClient 클래스를 직접 실행

### 5단계: 클라이언트 메뉴 사용

====== 상품 관리 시스템 접속 ======

1.목록조회 2.상세조회 3.추가 4.삭제 5.종료
번호입력 >>

- **1 입력**: 전체 상품 목록 조회
- **2 입력**: 상품 ID 입력 후 상세 정보 조회
- **3 입력**: 상품명, 가격, 수량 입력하여 추가
- **4 입력**: 삭제할 상품 ID 입력
- **5 입력**: 프로그램 종료

---

## 📝 통신 프로토콜

### 요청 (RequestDto)

{  "method": "get|post|delete",
  "querystring": {"id": 1},
  "body": {"name": "상품명", "price": 10000, "qty": 5}
}

### 응답 (ResponseDto)

{  "msg": "ok|error",
  "body": "JSON 형태의 데이터"
}

---

## 📌 주요 클래스 설명

| 클래스 | 설명 |
|--------|------|
| MyServer | Socket 서버 메인 클래스, 클라이언트 요청 처리 |
| MyClient | Socket 클라이언트 메인 클래스, 사용자 인터페이스 |
| ProductService | 상품 관련 비즈니스 로직 처리 |
| ProductRepository | 데이터베이스 CRUD 작업 처리 |
| DbConnection | MySQL 연결 관리 |
| RequestDto / ResponseDto | 클라이언트-서버 간 통신 데이터 구조 |

---

## 🛠️ 문제 해결 (Troubleshooting)

| 문제 | 해결책 |
|------|-------|
| Connection refused | MySQL이 실행 중인지 확인 |
| Access denied | DbConnection에서 MySQL 사용자명/비밀번호 확인 |
| Database does not exist | MySQL에서 productdb 생성 확인 |
| 클라이언트 연결 실패 | 서버가 먼저 실행되어 있는지 확인 |