# Push notification server

This push notification server is meant for the push notification proof of concept. This API is written in Java with Maven. The framework of choice is the Spring Framework.

## Installation

To install the API, you need to clone this repository.

To run the API, you need to run the maven command like shown below.

```bash
mvn spring-boot:run
```

## Usage

When you have runned the API, you can access multiple endpoints.
All these endpoints are described in Swagger. You can access that the same way you start the API. You just need to hit a different endpoint. And that endpoint is

```bash
localhost:8080/swagger-ui.html
```

## Testing

Testing is done through [Restassured](https://rest-assured.io/). The reason I have chosen for Restassured it that it will help me test the endpoints in a better way. Because there will be no database behind the API, the tests do not effect existing data.

To run the tests you can execute the maven command for tests.

```bash
mvn test
```

## Deployment

This application is hosted on Heroku. The frontend will connect to the following [link](https://be-push-notifications.herokuapp.com/).
