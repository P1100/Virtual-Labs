# AI 2020 VirtualLabs Project - Front end

## Notable Features:
- Nested routing
- Update to angular 10, and last version of Spring Boot
- Checkbox gmail style

## Dependencies
- moment js
- `ng add @ng-bootstrap/ng-bootstrap`

## Build
- `npm install` 
- `ng build –prod`
 
## Usage
- `npm start`
 - Production with SSL: `npm serve-prod-ssl`

## Update 
To update npm on windows, install newer nodejs. 
### Npm
- `npm install npm@latest -g` 
- `npm list -g` 
- `npm update` 
### Angular
- `ng update --all=true --allow-dirty --force`
- Or: `ng update @angular/cli @angular/core`


## Custom settings
* In node_modules/json-server-auth/dist/constants.js, changed from '1h' to 
`exports.JWT_EXPIRES_IN = '999999h';`
* In app.e2e-spec.ts
    `browser.sleep(10000);`
* In tslint.json 
```
"no-trailing-whitespace": false
    "variable-name": {
      "options": [
        "allow-leading-underscore", 
```
* In tsconfig.json  
       `"angularCompilerOptions": {
            "enableIvy": false,  `
* In environment.ts and environment.prod.ts (in assets)  
  `baseUrl: 'http://localhost:8080/API'`
* In karma.conf.js  (default)
`logLevel: config.LOG_INFO,`  
`reporters: ['progress', 'kjhtml'],`
