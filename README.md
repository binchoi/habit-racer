# 💡 Topic

- **혁신적인 경쟁형 해빗트레커: 해빗레이서**

# 📝 Summary

KR) 실제로 제가 필요하다고 느껴서 제작하게 된 웹서비스입니다. 코딩 테스트 연습이든 운동이든 새로운 습관을 만드는 것이 어렵게 다가왔던 적이 많았는데, 이 외롭고 버거운 과정을 더욱 재미있고 몰입감 있게 변화시키기 위해 가족/친구들과 함께 경쟁하며 습관을 만들어갈 수 있는 해빗트레커를 구현하였습니다. 유저들이 지정한 (user-selected) 내기를 걸고 레이스를 진행하여 양 유저의 몰입도와 습관화 성공률을 한 층 더 높였습니다. 새로운 습관을 만드는 과정이 혼자만의 외로운 싸움처럼 느껴지지 않도록 레이스 내에서 메시지를 주고, 받을 수 있는 기능을 더하였습니다.

EN) This is a webservice that sprung from my own needs. Whether it be practicing for coding interviews or exercising regularly, I found habit building to be an incredibly difficult and lonely journey. To transform this uphill battle into an immersive and interactive recreation, I created **HabitRacer** - a competitve habit tracker that allows users to compete with their friends and family members over user-selected wagers while building new habits together. Users create/join races and record their daily success whenever they successfully keep to their habit; the user that recorded more successes at the end of the race is crowned champion and claims the wager. For each race, the service provides race statistics and visualizes the progress of each driver on a race track to keep up the user's motivation!

# ⭐️ Key Function

- 레이스의 이름, 시작/종료 날짜, 내기/상금, 본인이 기르고 싶은 습관 등을 입력하여 1:1 **해빗레이스** 등록
- 레이스 내에서 양 유저의 성과를 기본적인 스탯으로 공유하고 레이싱카로 시각화하여 보여줌
- Success Record를 기록할때 상대 유저에게 메시지 전송 가능 (실시간 X)
- 엔진엑스의 리버스 프록시 기능을 통해 무중단 서비스 제공

## ✨ Functions Coming Soon
- 2인 이상 참가 가능한 해빗레이스 지원을 추가하기 위해 데이터베이스 재설계 예정
- 친구 목록, 앱 내에서 유저들 간의 레이스 초대

<!-- - Supports 1:1 races (2+ players race coming soon)
- Displays the user's on-going races and completed races in order of  -->

# 🛠 Tech Stack

`Java`, `Spring Boot`, `AWS` (`EC2`, `S3`, `RDS`, `Route53`, `CodeDeploy`), `MariaDB`, `TravisCI`, `Nginx`, `Spring Data JPA`,  `Gradle`, `k6`, `HTML5`, `Mustache`, `JUnit`

# ⚙️ Architecture

- `Spring MVC`

# 🤚🏻 Part

- **개인 프로젝트 (기획, 개발, 디자인 등)**

# 🤔 Learned
- Spring MVC 프레임워크을 이용하여 **MVC** 디자인 패턴 적용과 REST API 개발 경험
- 테스트 코드와 커버리지의 중요성을 경험하여 Test-Driven Development (TDD) 도입 그리고 그로 인한 생산성 증가 경험
- Spring JPA를 사용한 간단한 RDBMS/MariaDB 디자인 및 사용 경험
- Spring Security를 통한 Attribute Based Access Control (ABAC) 구현
- 유효성 체크 (i.e. Validation) 공통 모듈 생성을 통한 폼 내에서 graceful한 예외 처리
- k6를 이용한 기본적인 성능/부하 테스트와 [결과 분석](https://www.overleaf.com/read/pwbsmxymfdgv) 
- AWS 기반의 서비스 운영 경험
- 이해하기 쉬운 코드의 중요성을 몸소 체험
