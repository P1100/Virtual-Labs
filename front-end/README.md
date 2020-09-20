# AI 2020 VirtualLabs Project - Front end

## Dependencies
- moment js
- `ng add @ng-bootstrap/ng-bootstrap`

## Build
- `npm install` 
- `ng build –prod`
 
## Usage
- `npm start`
 - Production with SSL: `npm serve-prod-ssl`

## Update dependencies
#### NPM on Windows
To update npm on windows, install newer nodejs. Or:
- `npm install npm@latest -g` 
- `npm list -g` 
- `npm update` 
#### NPM on Ubuntu 18 LTS (https://www.digitalocean.com/community/tutorials/how-to-install-node-js-on-ubuntu-18-04)
- `cd ~` 
- `curl -sL https://deb.nodesource.com/setup_10.x -o nodesource_setup.sh` 
- `sudo bash nodesource_setup.sh` 
- `sudo apt install nodejs` 
## Update Angular Front-end App
- `ng update --all=true --allow-dirty --force`
- Or: `ng update @angular/cli @angular/core`

## Custom settings
* In node_modules/json-server-auth/dist/constants.js, changed from '1h' to 
`exports.JWT_EXPIRES_IN = '999999h';`p
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
  `baseUrl: 'http://localhost:8080/api'`
* In karma.conf.js  (default)
`logLevel: config.LOG_INFO,`  
`reporters: ['progress', 'kjhtml'],`
