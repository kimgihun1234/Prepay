# 사용 도구
- CI/CD: Jenkins
- 형상 관리: Gitlab
- 프로젝트 기록: Notion, Mattermost
- 이슈 관리: Jira
- 디자인: Figma

# 개발 환경

### Server
- Ubuntu: `22.04.5 LTS`
- Docker: `24.0.7`
  - mysql: `8.0.41`
- Nginx: `1.18.0`

### Front-end
- Android Studio Ladybug: `2024.2.1`

### Back-end
- IntelliJ: `2024.3.2.2`
- Springboot: `3.4.1`
- Open JDK: `17`

# gitignore 민감 정보
- .env (S12P11D111/BE/PrePay 에 위치)
- application-secret.yml (S12P11D111/BE/PrePay/src/main/resources 에 위치)

## .env

DB_URL=
DB_USERNAME=
DB_PASSWORD=s

DB_PROD_URL=
DB_PROD_USERNAME=
DB_PROD_PASSWORD=

JWT_SECRET=

BOOTPAY_PRIVATE_KEY=
BOOTPAY_APPID=

FCM_API_URL=

KAKAO_CLIENT_ID=

REDIS_PASSWORD=
REDIS_PORT=
REDIS_HOST=

# 서버 환경 구축
```bash
# 도커 설치
$ sudo apt-get update
$ sudo apt-get docker-ce
$ sudo systemctl start docker
$ sudo systemctl enable docker

# MySQL 컨테이너 설치
$ docker pull mysql 
$ docker start mysql


docker run -d --name jenkins -v /var/run/docker.sock:/var/run/docker.sock -v /usr/bin/docker:/usr/bin/docker -v jenkins-data:/var/jenkins_home -p 18080:8080  jenkins/jenkins:lts

# Nginx 설정
## nginx.conf
기본 설정과 동일

### sites-available/default

server {
  listen 80 default_server; #80포트로 받을 때
  #server_name i12d111.p.ssafy.io; #도메인주소, 없을경우 localhost
  location ^~ /.well-known/acme-challenge/ {
        root /var/www/html;
   }
  location / {
        return 301 $host$request_uri;
   }
}
server{
  listen 443 ssl http2;
  server_name i12d111.p.ssafy.io;

  # ssl 인증서 적용하기
  ssl_certificate /etc/letsencrypt/live/i12d111.p.ssafy.io/fullchain.pem;
  ssl_certificate_key /etc/letsencrypt/live/i12d111.p.ssafy.io/privkey.pem;

  proxy_set_header Host $http_host;
  proxy_set_header X-Real-IP $remote_addr;
  proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
  proxy_set_header X-Forwarded-Proto $scheme;

   # 기본 CORS 설정
  add_header 'Access-Control-Allow-Origin' '*' always;
  add_header 'Access-Control-Allow-Methods' 'GET, POST, PUT, DELETE, PATCH, OPTIONS' always;
  add_header 'Access-Control-Allow-Headers' 'DNT,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Range,Authorization' always;
  add_header 'Access-Control-Expose-Headers' 'Content-Length,Content-Range' always;

  location / { # location 이후 특정 url을 처리하는 방법을 정의(여기서는 / -> 즉, 모든 request)
    # 프리플라이트 요청 처리
    if ($request_method = 'OPTIONS') {
      add_header 'Access-Control-Allow-Origin' '*';
      add_header 'Access-Control-Allow-Methods' 'GET, POST, PUT, DELETE, PATCH, OPTIONS';
      add_header 'Access-Control-Allow-Headers' 'DNT,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Range,Authorization';
      add_header 'Access-Control-Max-Age' 1728000;
      add_header 'Content-Type' 'text/plain; charset=utf-8';
      add_header 'Content-Length' 0;
      return 204;
    }
    proxy_pass http://43.202.43.14:8080; # Request에 대해 어디로 리다이렉트하는지 작성. 8080 -> 자신의 springboot app이사용하는 포트
  }

  location /jenkins/{
    proxy_pass http://43.202.43.14:18080; # Request에 대해 어디로 리다이렉트하는지 작성. 8080 -> 자신의 springboot app이사용하는 포트
    proxy_http_version 1.1;
    proxy_set_header Upgrade $http_upgrade;
    proxy_set_header Connection "upgrade";  # WebSocket 지원
  }

}


## 도커 실행 명령어

docker run -d --name ${DOCKER_CONTAINER} \
    -p ${DOCKER_PORT}:${DOCKER_PORT} \
    -e SPRING_PROFILES_ACTIVE=prod \
    -e SERVER_PORT=${DOCKER_PORT} \
    -e DB_PROD_URL=${DB_URL} \
    -e DB_PROD_USERNAME=${DB_USERNAME} \
    -e DB_PROD_PASSWORD=${DB_PASSWORD} \
    -e JWT_SECRET=${JWT_SECRET} \
    -e BOOTPAY_PRIVATE_KEY=${BOOTPAY_PRIVATE_KEY} \
    -e BOOTPAY_APPID=${BOOTPAY_APPID} \
    -e client_secret=${client_secret} \
    -e FCM_API_URL=${FCM_API_URL}\
    -e KAKAO_CLIENT_ID=${KAKAO_CLIENT_ID} \
    -e TZ=Asia/Seoul\
    ${DOCKER_IMAGE}:latest
    
    sh 'gradle clean build'
    sh 'ls -al $(pwd)/build/libs'
    sh 'cp build/libs/PrePay-0.0.1-SNAPSHOT.jar .'
    sh 'docker build -t ${DOCKER_IMAGE}:latest .'

