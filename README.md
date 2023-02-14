- Teachers are able to teach various subjects and assign marks to students in the classes they teach. When a teacher is added to a school class, a corresponding TeacherInClass entity is automatically created. If the teacher already has a TeacherInClass entity, it is simply added to the class.

- Students can view their marks, average marks grouped by subject, and a list of teachers who teach subjects in their class.

- Administrators can add new classes, subjects, teachers, and students. When a new student or teacher is created, a user entity is generated and an email is sent to confirm the account. The account remains inactive until the confirmation using token sent with email.

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
- QueryDSL <img width="28px" src="https://user-images.githubusercontent.com/112166269/216625032-7f0af41a-3ecd-4646-a254-1abbdd4db8a0.png"/>



Tests:
- JUnit 5 <img width="54px" src="https://upload.wikimedia.org/wikipedia/commons/5/59/JUnit_5_Banner.png"/>
- Mockito <img width="70px" src="https://raw.githubusercontent.com/mockito/mockito.github.io/master/img/logo%402x.png"/>
- Testcontainers <img width="28px" src="https://avatars.githubusercontent.com/u/13393021?s=200&v=4"/>

Other:
- Docker <img width="30px" src="https://www.docker.com/wp-content/uploads/2022/03/Moby-logo.png"/> / Docker compose<img width="57px" src="https://miro.medium.com/max/453/1*_5tOkcXb7RaVvjYpSqZXpg.png"/>
- Git <img width="23px" src="https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Git_icon.svg/1024px-Git_icon.svg.png"/>

# Endpoints:
![localhost_8080_swagger-ui html](https://user-images.githubusercontent.com/112166269/218687664-561c953a-7f83-4fab-82fa-f2fb5dd7be4f.png)



# Database diagram:
![diagram](https://user-images.githubusercontent.com/112166269/213867353-9eb68e44-46b5-430b-9b65-8899cb3f63bc.PNG)


