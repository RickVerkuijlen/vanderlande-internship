# Backend Chat Proof of Concept

This backend server is meant for the chat proof of concept. This websocket server is written in Java with Maven. The framework of choice is the Spring framework.

## Installation

To install this API, you need to clone this repository.
To run the API, you need to run the Maven command like shown below.

```bash
mvn spring-boot:run
```

## Usage

When you run the websocket, the frontend can connect to it using the following link.

```bash
http://localhost:8080/vibes-chat
```

## Testing

Testing is done through [JUnit](https://junit.org/junit5/). These tests are ran every time the code is pushed to Bamboo. If these tests fail, the build will fail as well. You can execute the tests yourself by executing the following code:

```bash
mvn test
```
