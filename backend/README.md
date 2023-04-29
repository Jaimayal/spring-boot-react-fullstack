# Spring Boot Backend
Here's the main backend for the Customers API project.

## Technologies used
### Development
- Spring
  - Spring Boot 3.0
  - Spring Web
  - Spring Data JPA
  - Spring Data JDBC
- Java 17
  - Flyway
- Maven
- PostgreSQL 15
- Docker
  - docker-compose

### Testing
- Java 17
  - JUnit 5
  - Mockito
  - Java Faker
  - Testcontainers
- Maven
  - surefire (Unit Tests)
  - failsafe (Integration Tests)

### Deploy
- Docker
- Maven
  - spring-maven 
  - build-helper
  - jib (Docker Image creation and Push)
- AWS
  - Elastic Beanstalk
  - EC2
  - ECS
  - RDS
  - Cloudformation