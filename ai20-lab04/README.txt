## Ai20Lab05

# Lista dei nomi/matricole dei componenti del gruppo
PIETRO GIASONE 160557

# Indicazioni per la verifica delle funzionalità dell’applicazione sviluppata
L'applicazione sviluppata segue le indicazioni date per la stesura dell'esercitazione numero cinque. L'app é molto user friendly e l'uso dovrebbe essere facile ed istantaneo.
Si allegano piú screenshots in modo da avere una visione chiara ed immediata dell'elaborato fatto (nella cartella _screenshots, perché sono molti)

Come features aggiuntive o da menzionare:
- Il routing é stato implementato con una struttura nested a piú livelli, in un ottica di progetto e come suggerito nel testo
- É stato implementata anche la logica per gestire corsi multipli, in modo completo.
- SSL implementato come npm script (insieme a prod)

# Istruzioni di avvio (vedere file package.json per altri comandi e dettagli)
- npm install
- npm start
---- ssl&prod: npm run startAll-windows-prod-ssl
---- linux/mac(?): npm run startAll-linux

# Comandi usati ~ (windows10)
- npm install -g @angular/cli
- ng new ai20-lab04
- cd ai20-lab04
- ng add @angular/material
- npm install -D json-server json-server-auth
- npm install moment
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
ng generate component auth/login-dialog --flat --module=app
ng generate component auth/register-dialog --flat --module=app
ng generate service auth/auth --flat

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

In environment.ts e environment.prod.ts (in assets)
     urlHttpOrHttpsPrefix: 'http'
     urlHttpOrHttpsPrefix: 'https'



