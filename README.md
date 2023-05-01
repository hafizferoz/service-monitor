# AngularControlApp

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 9.1.15 and spring boot version 2.7.11.
The project monitors application status on a dashboard. Add applications to the existing monitor to check the health of the application UP/DOWN and DOWN TIME/UP TIME etc. The application must have actuator dependecy installed.

![alt text](https://github.com/hafizferoz/service-monitor/blob/master/dashboard.PNG)

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

Run `mvn spring-boot:run` for spring boot server. Navigate to `http://localhost:8081/`.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `--prod` flag for a production build.

Run `mvn clean install -DskipTests=true` for maven build.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI README](https://github.com/angular/angular-cli/blob/master/README.md).
