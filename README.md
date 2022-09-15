# SpringCafeProject


# 🌲  SpringCafeProject
말하는 감자의 스프링 게시판 개인 프로젝트

![Java](https://img.shields.io/badge/JAVA-007396?style=for-the-badge&logo=java&logoColor=white)
![Spring-Boot](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white)
![JPA](https://img.shields.io/badge/jpa-00555?style=for-the-badge&logo=jpa&logoColor=white)
![Redis](https://img.shields.io/badge/redis-232F3E?style=for-the-badge&logo=redis&logoColor=red)
![MySQL](https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![AWS](https://img.shields.io/badge/aws-232F3E?style=for-the-badge&logo=aws&logoColor=white)


</br>
<details markdown="1">
<summary> 명세:</summary><br>


<br>
ERD:<br>
<img width="992" alt="스크린샷 2022-08-22 오후 4 38 40" src="https://user-images.githubusercontent.com/64303390/185865931-837311f3-3f86-462d-ab25-efd6a5aa6f17.png">
</details>

</br>


</br>

웹 카페 게시판 어플리케이션을 구현하는 프로젝트입니다.

## 📖 개요

 코드스쿼드 과정 참여 후 자가진단을 위해 진행해보는 개인 프로젝트입니다.
 사용자 게시판을 여러가지 방법으로 구현하고 다양한 기능을 넣어 고도화 시켜보고자 시작합니다.


## 💎 Main Features

- 기본적인 게시판 CRUD
- 로그인: github oauth + JWT
- 기본 CI/CD
- 배포시 자동화 된 테스트 인프라 생성, 부하테스트 후 결과 슬랙 전송 (Ngrinder Rest API, Github Action, AWS CLI, SLACK)
- 레디스 캐싱 (조회수 update 배치처리, 게시물 단건조회 등)

## 📐 Deployment/Diagram


## 🖥️ Build Environment
### gradle build 
## (Windows, Bash)
```
$ ./gradlew build
$ java -jar -Dspring.profiles.active=test gesipan-0.0.1-SNAPSHOT.jar 
```
## (Mac)
```

```


## 📃 API Specification
https://www.postman.com/aaron2-postman/workspace/zzangmin-springcafe

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


