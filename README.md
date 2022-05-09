# 💡 Topic

- **혁신적인 경쟁형 해빗트레커: 해빗레이서**

# 📝 Summary

<!-- 실제로 제가 필요하다고 느껴서 제작하게 된 웹서비스 입니다. 코딩 테스트 연습이든 운동이든 새로운 습관을 만드는 것이 상당히 어렵게 다가왔던 적이 많았는데, 이 외롭고 버거운 과정을 더욱 재미있고 몰입감 있게 변화시키기 위해 가족/친구들과 함께 경쟁하며 습관을 만들어갈 수 있는 해빗트레커 서비스를 구현하였습니다. 레이스를 만들고 경쟁자를 초대하여 매일 The service allows users to create and/or join races with their friends and record their daily progress. The progress of their competitor is visualized in the race dashboard to motivate users to keep to their habits. 레이스를 만들어 경쟁자를 초청하면 서로 정해진 앱에 물품을 등록해두면 주기적으로 푸시 알림을 제공하여 사용자가 물품 구매를 잊지 않도록 도와주는 서비스입니다. 만약 쇼핑몰 링크를 복사해둔 채 앱을 키게 되면, 자동으로 물품을 인식하여 등록 페이지로 안내해주어 편의성을 강조했습니다.  -->

This is a webservice that sprung from my own needs. Whether it be practicing for coding interviews or exercising regularly, I found habit building to be an incredibly difficult and lonely journey. To transform this uphill battle into an immersive and interactive recreation, I created **HabitRacer** - a competitve habit tracker that allows users to compete with their friends and family members over user-selected wagers while building new habits together. Users create/join races and record their daily success whenever they successfully keep to their habit and the user that recorded more successes at the end of the race is crowned champion and claims the wager. For each race, the service provides race statistics and visualizes the progress of each driver on a race track to keep up the user's motivation!

# ⭐️ Key Function

<!-- - **구매할 물품**의 이름, 이미지, 가격, 메모, 중요도 등을 입력하여 **앱에 등록**
- 사용자가 등록해둔 물품을 **최신순, 중요도순, 가격순으로 정렬**하여 보여줌
- 사용자가 등록해둔 물품 키워드 검색 기능 제공 (RxJava Debounce 적용)
- 아직 구매하지 않은 물품에 대하여 **정기적으로 푸시알림 제공 (WorkManager)**
- **클립보드에 쇼핑몰 링크가 감지**되면, **물품 정보를 자동으로 채워줌 (OpenGraph 파싱)** -->

# 🛠 Tech Stack

`Java`, `Spring Boot`, `AWS` (`EC2`, `S3`, `RDS`, `Route53`, `CodeDeploy`), `MariaDB`, `TravisCI`, `Nginx`, `Spring Data JPA`,  `Gradle`, `HTML5`, `Mustache`, `JUnit`

# ⚙️ Architecture

- `Spring MVC`

# 🤚🏻 Part

- **개인 프로젝트 (기획, 개발, 디자인 등)**

# 🤔 Learned

<!-- - JetPack **`Room`** 로컬 데이터베이스 사용법을 알게 되었음.
- **MVVM** 패턴을 처음으로 도입해보며, **패턴에 대한 이해**를 높일 수 있었음.
- **`Koin`** 을 통해 DI 를 처음으로 적용해보며, **의존성 주입의 편리함**을 깨닫게 되었음.
- **`RxJava`** 를 통해 **`EditText`** 입력값을 **`Observable`** 로 받아 **쿼리 디바운싱 스킬**을 적용해보았음.
- **`WorkManager`** 를 활용하여 **`특정 백그라운드 동작을 예약`** 하는 기능을 구현하는 방법을 알게 되었음.
- **`OpenGraph`** Tag 개념을 알게 되었고, 이를 파싱하여 **미리보기 기능**을 구현해볼 수 있었음. -->
