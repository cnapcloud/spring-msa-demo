# Spring MSA Demo

이 프로젝트는 Spring 기반으로 마이크로서비스 애플리케이션을 개발하기 위한 예제입니다.  
마이크로서비스 아키텍처에서 필요한 **프로젝트 구조, 공통 기능, 서비스 구성, 빌드·테스트·패키징 방법**을 제공합니다.


## 1. 주요 기술 및 구성 요소

**개발 환경**
- JDK: 17
- Spring Boot: 3.3.4
- Gradle: 8.5
- 프로젝트 구조: Gradle 기반 멀티 모듈 프로젝트

**주요 기능**
- OpenAPI(Swagger) 설정을 통한 REST API 문서화
- 전역 예외 처리(Global Exception Handling) 구성
- 인증(Authentication) 및 권한 관리(Authorization) 구현
- JPA Auditing 및 페이징(Pagination) 처리 지원
- MapStruct를 활용한 DTO ↔ Entity 간 자동 매핑
- Snake Case 기반의 REST API 요청/응답 데이터 변환
- Mock 기반 단위 테스트 및 jacoco 테스트 커버리지 리포트 생성

**빌드 및 배포**
- 멀티 플랫폼 Docker 이미지 빌드 지원
- Makefile을 이용한 빌드·테스트·패키징 자동화


## 2. 애플리케이션 빌드

`Makefile`에 정의된 타겟(target)을 실행하여 소스 코드를 빌드하고, 테스트 및 이미지 생성을 수행할 수 있습니다.

```
make build           #  소스코드 빌드
make report          #  테스트케이스 실행 및 코드커버리지 리포트
make docker-build    # 이미지 빌드 
````


## 3. 애플리케이션 실행

먼저 Keycloak 클라이언트 정보를 환경변수로 설정합니다.

```
export KEYCLOAK_URL=https://keycloak.cnap.dev
export REALM=cnap
export CLIENT=msa
export CLIENT_SECRET=ZVpV2w2D2QwOnDVinLHBX2blEwf8JL60
```

애플리케이션을 실행하기 위해 위에서 빌드한 이미지를 이용해 Docker 컨테이너를 실행합니다.  
entrypoint.sh 스크립트는 기본적으로 project-service를 실행하도록 설정되어 있습니다.

```
docker run --rm -p 8080:8080 \
  -e KEYCLOAK_REALM_URL="${KEYCLOAK_URL}/realms/${REALM}" \
  -e KEYCLOAK_CLIENT_ID="${CLIENT}" \
  -e KEYCLOAK_CLIENT_SECRET="${CLIENT_SECRET}" \
  --add-host=keycloak.cnap.dev:192.168.64.2 \
  cnapcloud/spring-msa-demo:latest
```

브라우저에서 아래 주소로 접속하여 project-service가 정상적으로 실행되는지 확인합니다.
```
http://localhost:8080/swagger-ui/index.html
```


## 4. 가이드

**인증 및 권한 설정**  

Keycloak을 통한 인증 및 권한 구성 방법에 대한 자세한 내용은 아래 블로그 포스트를 참고하세요.  
[Spring Security와 Keycloak 연동 가이드](https://cnapcloud.com/blog/spring-keycloak/)

