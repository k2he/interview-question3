How to run the app
==================
1) in command line: mvn spring-boot:run
2) in eclipse (if you installed spring suite): right click on DemoApplication.java -> Run as -> Spring Boot App
2) to view Jacoco code coverage: open /target/site/jacoco/index.html (as for this project right now is 97%) 

## Things about the Code Design
1) added <scope>provided</scope> to lombok maven dependency, since lombok only used at compile time, no need to include into the build package.
2) added jacoco-maven-plugin maven dependency, to view code coverage easily.
3) added MessageService which loads message from /src/resouce/messages.properties, all messages are in a central place. In additional, it give us potential to load different message depends on local (eg: messages_fr.properties) 
4) used contrctuor injection, since this is more sugguested way over field injection: https://reflectoring.io/constructor-injection/
5) added GlobalExceptionHandler to handle resource not found exception, it should also handle other exceptions like Business exception, System Exception, Validation Exception and so on there. 

## Test cases
1) added Junit test cases for repository, service and controller, one for each. Testing normal case + exception case.
2) added one Integration test case for Controller, since Integration test do not use mock, so testing controller is good enough.
