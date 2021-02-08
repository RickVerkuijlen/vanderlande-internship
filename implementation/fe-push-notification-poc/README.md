# BrowserNotificationPoc

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 10.1.0.

## Build

Run `ng build --prod` to build the project. The build artifacts will be stored in the `dist/` directory.

## Development server

You need to run a HTTP server to register service workers on you local machine. Before you run a development server, be sure that you have runned the `ng build --prod` command first. After that, you can run `http-server -p 8080 -c-1 ./dist/browser-notification-poc` for a dev server. Navigate to `http://localhost:8080/`.
When you want to connect to the REST server, you first need to follow the instructions to run the [REST server](https://devstash.vanderlande.com/projects/VUX/repos/vi_ux_app/browse/implementation/be-push-notification-poc).

<!-- ## Live server
If you want to test this on a mobile device, you can go to the [Test environment](https://vanderlandetest-7b711.web.app/). The newest software is on this URL and you can test the notifications on your mobile device (Android with Google Chrome). -->

## Running unit tests

Run `npx jest` to execute the unit tests via [Jest](https://jestjs.io/).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI README](https://github.com/angular/angular-cli/blob/master/README.md).
