Teacher:<br>
Teachers can teach many subjects, add marks to students from classes in which they teach the subject which is able due to TeacherInClass entity which is automatically created when adding teacher to school class.
If teacher already has it's TeacherInClass counterpart, it is added to the class.<br>
Student:<br>
Students can view their marks, average marks grouped by subject and list of teachers of subjects in class they belong to.
Admin: <br>
Admins can add new classes, subjects, teachers and students. After creating a student or teacher, a user entity is created, an email is sent, and the account is not available until an email is not confirmed.
# How to run:
To run the application it is necessary to have running postgreSQL docker container (you can use docker-compose file in docker folder) and run application with "prod" profile active
# Technologies used across the project:
Backend:
- Java <img width="25px" src="https://cdn-icons-png.flaticon.com/512/226/226777.png"/>
- Spring Boot<img width="40px" src="https://user-images.githubusercontent.com/33158051/103466606-760a4000-4d14-11eb-9941-2f3d00371471.png"/>(Spring Web, Spring Security (JWT <img width="18px" src="https://cdn.cdnlogo.com/logos/j/20/jwt.svg"/>), Spring Data JPA, Validation, Java Mail Sender)
- Maven <img width="50px" src="https://maven.apache.org/images/maven-logo-white-on-black.purevec.svg"/>
- PostgreSQL <img width="24px" src="https://upload.wikimedia.org/wikipedia/commons/thumb/2/29/Postgresql_elephant.svg/1024px-Postgresql_elephant.svg.png"/>
- Liquibase <img width="80px" src="https://www.liquibase.com/wp-content/uploads/2020/05/Liquibase_logo_horizontal_RGB.svg"/>
- Lombok <img width="40px" src="https://kodejava.org/wp-content/uploads/2018/12/lombok.png"/>
- Swagger <img width="22px" src="https://upload.wikimedia.org/wikipedia/commons/a/ab/Swagger-logo.png"/>
- Apache Commons Lang  <img width="18px" src="https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Apache_Feather_Logo.svg/1200px-Apache_Feather_Logo.svg.png"/>
- MapStruct <img width="80px" src="https://user-images.githubusercontent.com/112166269/214404167-1614aaf4-fb84-412f-91bd-9ad1b09f3ad4.png"/>
- QueryDSL

Tests:
- JUnit 5 <img width="54px" src="https://upload.wikimedia.org/wikipedia/commons/5/59/JUnit_5_Banner.png"/>
- Mockito <img width="70px" src="https://raw.githubusercontent.com/mockito/mockito.github.io/master/img/logo%402x.png"/>
- Testcontainers <img width="28px" src="https://avatars.githubusercontent.com/u/13393021?s=200&v=4"/>

Other:
- Docker <img width="30px" src="https://www.docker.com/wp-content/uploads/2022/03/Moby-logo.png"/> / Docker compose<img width="57px" src="https://miro.medium.com/max/453/1*_5tOkcXb7RaVvjYpSqZXpg.png"/>
- Git <img width="23px" src="https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Git_icon.svg/1024px-Git_icon.svg.png"/>

# Endpoints:
![swagger](https://user-images.githubusercontent.com/112166269/209846352-1ff07d10-eb16-45e0-b4de-d34625053b1a.png)


# Database diagram:
![diagram](https://user-images.githubusercontent.com/112166269/213867353-9eb68e44-46b5-430b-9b65-8899cb3f63bc.PNG)


