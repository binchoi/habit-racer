# 💡 Topic

- **HabitRacer**: A competitive, habit tracker that fills two needs with one deed ⚡

# 📝 Summary

This is a webservice that sprung from my own needs. Whether it be practicing for coding interviews or exercising regularly, I 
found habit building to be a difficult and lonely journey. To transform this uphill battle into an immersive and interactive 
recreation, I created HabitRacer — a competitve habit tracker that allows users to compete with their friends and family members 
over user-selected wagers while building new habits together. Users create/join races and record their daily success whenever they 
successfully keep to their habit; the user that recorded more successes at the end of the race is crowned champion and claims the 
wager. For each race, the service provides race statistics and visualizes the progress of each driver on a race track to boost the 
user's motivation!

# ⭐️ Key Function
- Create/Join a 1:1 Habit Race by submitting the race name, start/end date, wager, and habit-to-build
- Visualize the users’ progress in a race with cars racing down a track and Race Stats
- Send messages to your oponent when posting a Success Record (Real-Time X)
- Provide interruption-free, zero-downtime service by using Nginx reverse proxy

## ✨ Functions Coming Soon
- Habit races that accomodate 2+ users such that larger friend groups and families can compete in a single race. (Redesigning DB required)
- Ability for users to manage friend lists and send race invitations from within the app

# 🛠 Tech Stack

`Java`, `Spring Boot`, `AWS` (`EC2`, `S3`, `RDS`, `Route53`, `CodeDeploy`), `MariaDB`, `GitHub Actions`, `Nginx`, `Spring Data JPA`,  `Gradle`, `k6`, `HTML5`, `Mustache`, `JUnit`

# ⚙️ Architecture

- `MVC`

# 🤚🏻 Part

- **Solo Project (Planning, Development, Design, etc.)**

# 🤔 Learned
- Developed `REST API` and applied `MVC` design pattern using `Spring MVC` Framework
- Initiated Test-Driven Development (`TDD`) using `JUnit`/`SpringBootTest` for unit/integration testing and `JaCoCo` for code coverage assessment and drastically reduced the time required for testing and maintenance activities
- Designed a schema for `RDBMS/MariaDB` and utilized `Spring Data JPA` to interact with the DB in an object-oriented manner
- Implemented `Attribute Based Access Control` (ABAC) by utilizing `Spring Security`
- Constructed a separate Validation module that handles graceful exception handling in forms
- Conducted basic performance and load testing using `k6` and analyzed the [results](https://www.overleaf.com/read/pwbsmxymfdgv)
- Experienced the operation of running a service on `AWS`
- Gained an appreciation for readable, clean code

# 📺 Demo Video
[![demo-video](https://img.youtube.com/vi/sS9pXB-_6YA/0.jpg)](https://www.youtube.com/watch?v=sS9pXB-_6YA)
