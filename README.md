# SpizarkaServlet
Database Servlet for Spizarka app (Spring/Hibernate/REST)

## Motivation

Servlet was created for ensure cloud database access for Android's application Spizarka (look at my Spizarka repo).
Working scheme is that user creates account for home pantry in Spizarka app and every changes in internal app database are instantly synchronizing with 
database in cloud. This ensure that, for example, when my wife use some products during the day I will know about it when I'll go 
shopping after work, beacuse my Spizarka client actualise internal database via Internet using this Servlet. Moreover I could send automatically 
generated shopping list by email to any person which I want at the moment.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

App was made with use Intellij IDEA environment, so the easiest way is open project file .iml


### Installing

To build .war file You must provide database to make integration test (od turn off them), as is described below.
You can use IDE or Maven for make package:

```
maven package
```

## Running the tests

Servlet has two test types:
- Unit tests for all Services
- Integration test for all Controllers

Running tests 
```
maven test
```

### Break down into end to end tests

Unit test need nothing more than Maven or IDE, but to start intergration test You need 
to connect database. Predefined database are MySQL on localhost, setting are in file
path: src\main\resources\application.properties

```
spring.datasource.url=jdbc:mysql://localhost:3306/spizarka
spring.datasource.username=login
spring.datasource.password=pass
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```


## Built With

* [Spring](https://projects.spring.io/spring-boot/) - The web framework used, version 1.4.1
* [Maven](https://maven.apache.org/) - Dependency Management
* [JavaMail Api](http://mvnrepository.com/artifact/javax.mail/javax.mail-api) - MailService


## Authors

* **Bartłomiej Gątarski** - *All work* - [Gatar](https://github.com/Gatar)

