## Functionality

Tasklist is a RESTful API for flexible managing tasks. It performs following functionality:

- login and authentication using JWT tokens
- CRUD operations with tasks
- CRUD and some others operations with groups
- email notifications after registration and reminders about the expiration of a task
- different roles for users

You can access Swagger and see all available endpoints by visiting `http://localhost:8080/swagger-ui/index.html`

## Testing

All endpoints and logic are tested using Postman and JUnit tests.

Run [postman collection](https://github.com/isthatkirill/tasklist-spring-boot/blob/main/postman/tasklist.json) in Postman client to see test results.

Run `mvn clean verify` to see that all JaCoCo rules are passed.

Open `/target/site/jacoco/index.html` to see JaCoCo report.

Unit tests provide the following code coverage:

- Lines: 87%
- Classes: 83%
- Complexity: 79 %
- Branches: 76%

## Run the application

1. Clone this repository
   `git clone https://github.com/isthatkirill/tasklist-spring-boot.git`
2. Go to the directory with the program
   `cd tasklist-spring-boot`
3. Run docker containers using docker-compose
   `docker-compose up`
4. Good job! The application is running. Detailed information about the launch is available in the logs in the console.

## Environment

- `DB_HOST` - host of database
- `DB_USER` - username for database
- `DB_PASS` - database password
- `DB_NAME` - name of database
- `SECRET` - secret string for JWT tokens
- `ACCESS` - ttl for access tokens
- `REFRESH` - ttl for refresh tokens
- `MAIL_HOST` - host of mail
- `MAIL_PORT` - port of mail
- `MAIL_USERNAME` - name of the email used
- `MAIL_PASS` - password of the email used

## Technologies and libraries used

- Java 17
- Spring Boot 3.0.5
- Spring Mail
- Spring Security (JWT)
- Swagger
- JUnit 5
- Mockito
- Spring Data Jpa
- PostgreSQL
- H2
- Docker
- Lombok
- Mapstruct
- Liquibase
- JaCoCo plugin
