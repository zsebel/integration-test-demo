## Bitcoin application

Simple demo application for mentoring purposes to demonstrate and practice integration testing with various approaches.


#### CoinDesk
Under the hood Bitcoin application uses CoinDesk to fetch Bitcoin Price Indices from:

```
https://api.coindesk.com/v1/bpi/currentprice.json
```

### Testing the application
#### Running locally
There are a few options (from more flexible to least)
1. You can just use the SpringBoot run functionality that is default in IntelliJ or SpringTools in Eclipse
2. Start application with Maven
    ```
    mvn spring-boot:run
    ```
3. Package the jar file and run it
    ```
    mvn clean package
    java -jar bitcoing-appliction/target/bitcoin-app-exec.jar
    ```
   
#### Invoking the service
You can use your preferred way to make a GET request to the application:
```
http://localhost:8080/api/bitcoin/prices
```

### Integration tests
The following integration tests are available:
- NoMockingIntegrationTest
- ManualMockingIntegrationTest
- WireMockIntegrationTest
- MockMvcIntegrationTest
- MockRestServiceServerIntegrationTest
- CucumberIntegrationTest
- KarateIntegrationTest

### Exercise
Write a Spring Boot application that integrates `POST /public/v2/users` from https://gorest.co.in/ and exposes this functionality from your application.

When you are done, write three integration tests with the following approaches: 
- using a mock bean for GoRest client
- using WireMock Server
- using MockMvc






