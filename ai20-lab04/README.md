# Ai20Lab04

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 9.1.7.

## Comandi usati
- npm install -g @angular/cli
- ng new ai20-lab04
- cd ai20-lab04
- ng add @angular/material
- npm install -D json-server json-server-auth
- ng build –prod

ng generate component teacher/studentsCont --flat --module app
ng generate module app-routing-module --flat --module=app
ng generate component PageNotFound --module=app --inlineTemplate --inlineStyle
ng generate component OtherCourse --module=app --inlineTemplate --inlineStyle
ng generate component VmsCont --module=app
ng generate component GroupsCont --module=app
ng generate component AssignmentsCont --module=app
ng generate component Home --module=app
ng generate service services/student --flat

npm start
npm run jsonserver

## Comandi browser
http://localhost:3000/students?_expand=group

## Custom settings
In node_modules/json-server-auth/dist/constants.js, changed from '1h'
`exports.JWT_EXPIRES_IN = '999999h';`

In tslint.json
    "variable-name": {
      "options": [
        "allow-leading-underscore",


## Development server
Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

## Code scaffolding
Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build
Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `--prod` flag for a production build.

## Running unit tests
Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests
Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).

## Further help
To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI README](https://github.com/angular/angular-cli/blob/master/README.md).
