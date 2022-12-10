
# 🌲  SpringCafeProject
![Java](https://img.shields.io/badge/JAVA-007396?style=for-the-badge&logo=java&logoColor=white)
![Spring-Boot](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white)
![JPA](https://img.shields.io/badge/jpa-00555?style=for-the-badge&logo=jpa&logoColor=white)
![Redis](https://img.shields.io/badge/redis-232F3E?style=for-the-badge&logo=redis&logoColor=red)
![MySQL](https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![AWS](https://img.shields.io/badge/aws-232F3E?style=for-the-badge&logo=aws&logoColor=white)


SpringBoot를 사용하여 API 서버를 개발합니다.  
view는 없지만, API 성능 개선, 부하 테스트, 쿼리 튜닝, redis 캐싱, CI/CD + 부하테스트 자동화 등을 수행합니다
<br>

## 📖 개요

 사용자 게시판을 여러가지 방법으로 구현하고 다양한 기능을 넣어 고도화 시켜보고자 시작합니다.


## 💎 Main Features

- 기본적인 게시판 CRUD (게시글/댓글 CRUD, 게시글 임시저장, 로그인 및 인증/인가, 추천, 알림 등)
- 로그인: github oauth + JWT
- 기본 CI/CD
- 배포시 자동화 된 테스트 인프라 생성, 부하테스트 후 결과 슬랙 전송 (Ngrinder Rest API, Github Action, AWS CLI, SLACK)
- 레디스 캐싱 (조회수 update 배치처리, 게시물 단건조회 등)

\\<!-- ## 📐 Deployment/Diagram  -->


## 🖥️ Build Environment
### gradle build 
## (Windows, Bash)
```
$ ./gradlew build --exclude-task test
$ cd build/libs
$ java -jar gesipan-0.0.1-SNAPSHOT.jar --profile=test 
```
## (Mac)
```
$ ./gradlew build
$ cd build/libs
$ java -jar gesipan-0.0.1-SNAPSHOT.jar --profile=test 
```
<br>
ERD:<br>

![스크린샷 2022-10-16 오후 8 23 56](https://user-images.githubusercontent.com/64303390/196032614-d5ed12f4-455e-4ba3-beac-d900421d0535.png)

</br>

## 📃 API Specification
https://aaron2-postman.postman.co/documentation/19902575-7a0f9cca-b1f5-438b-bfe6-be4bb6d6a7ac/publish?workspaceId=8e03e661-4d79-462f-ae55-5061fce9248f

## 🏛️ Depedency Used
```
spring-boot-starter-web
spring-boot-starter-validation
spring-boot-starter-data-jpa
com.querydsl:querydsl-jpa
spring-boot-starter-data-redis
io.jsonwebtoken:jjwt:0.9.1
org.projectlombok:lombok
com.h2database:h2
mysql:mysql-connector-java
spring-boot-starter-test
```


