## Bitcoin application

Simple demo application for mentoring purposes to demonstrate and practice integration testing with various approaches.


#### Coinbase
Under the hood Bitcoin application uses Coinbase to fetch Bitcoin Price Indices from:

```
https://api.coinbase.com/v2/exchange-rates?currency=BTC
```

### Testing the application
#### Prerequisites

- Java version: 21
- Maven version: 3.6.3 and above (recommended 3.9.x)

#### Running locally
There are a few options (from more flexible to least)
1. Run the Spring Boot application from IntelliJ IDEA or SpringTools in Eclipse
2. Start application using the following Maven command
    ```
    mvn spring-boot:run
    ```
3. Package the application and run the jar file
    ```
    mvn clean package
    java -jar bitcoin-appliction/target/bitcoin-app-exec.jar
    ```
   
#### Invoking the service
You can use your preferred way to make a GET request to the application:
```
http://localhost:8080/api/v2/bitcoin?currency=USD
```

### Integration tests
The following integration tests are available:
- NoMockingIntegrationTest
- StubbedClientIntegrationTest
- MockBeanIntegrationTest
- WireMockIntegrationTest
- CucumberIntegrationTestRunner
- KarateWithWireMockIntegrationTest
- KarateWithMockServerIntegrationTest
- CircuitBreakerIntegrationTest
- CoinbaseTimeoutIntegrationTest
- RetryIntegrationTest

### Exercise
Write a Spring Boot application that integrates `POST /public/v2/users` from https://gorest.co.in/ and exposes this functionality from your application.

When you are done, write three integration tests with the following approaches: 
- using a stub that you create
- using @MockitoBean for GoRest client
- using WireMock Server
- additionally you can try one of the BDD approaches (Cucumber or Karate)






