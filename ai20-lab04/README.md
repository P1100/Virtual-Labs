# Ai20Lab04

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 9.1.7.

## Comandi usati
- npm install -g @angular/cli
- ng new ai20-lab04
- cd ai20-lab04
- ng add @angular/material
- npm install -D json-server json-server-auth
- ng build –prod
- npm start (linux: npm run startAll-linux)

ng generate component teacher/studentsCont --flat --module app
ng generate module app-routing-module --flat --module=app
ng generate component PageNotFound --module=app --inlineTemplate --inlineStyle
ng generate component OtherCourse --module=app --inlineTemplate --inlineStyle
ng generate component VmsCont --module=app
ng generate component GroupsCont --module=app
ng generate component AssignmentsCont --module=app
ng generate component Home --module=app
ng generate service services/student --flat
ng generate component auth/login-dialog --flat --module=app
ng generate component auth/register-dialog --flat --module=app
ng generate service auth/auth --flat

npm install moment

ng generate guard -d auth/auth --flat --implements CanActivate

ng generate component HomeTab --module=app --inlineTemplate --inlineStyle --minimal


## Comandi browser
http://localhost:3000/students?_expand=group

## Custom settings
In node_modules/json-server-auth/dist/constants.js, changed from '1h'
`exports.JWT_EXPIRES_IN = '999999h';`

In tslint.json
    "no-trailing-whitespace": false
    "variable-name": {
      "options": [
        "allow-leading-underscore",
        
In tsconfig.json
 "angularCompilerOptions": {
       "enableIvy": false,
       
In angular.json (aggiunto file proxy.conf.json) --> no, poi passato a usare scripts in package.json
        "serve": {
          "builder": "@angular-devkit/build-angular:dev-server",
          "options": {
            "browserTarget": "ai20-lab04:build",
            "proxyConfig": "src/proxy.conf.json"
          },
          "configurations": {
            "production": {
              "browserTarget": "ai20-lab04:build:production",
              "proxyConfig": "src/proxy.conf.json",
              "sslCert": "server.key",
              "sslKey": "server.crt"
            }
          }
        }

In environment.ts e version prod
     urlHttpOrHttpsPrefix: 'http'
     urlHttpOrHttpsPrefix: 'https'




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
