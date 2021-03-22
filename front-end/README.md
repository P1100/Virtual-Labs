# AI 2020 VirtualLabs Project - Front end

# Build
- `npm install`
- `ng build –prod`

# Usage
- `npm start`
- Production with SSL: `npm serve-prod-ssl`

## UPDATE

#### NPM on Windows

To update npm on windows, install newer nodejs. Alternatively:
```
npm install npm@latest -g
```

#### NPM on Ubuntu 18 LTS (https://www.digitalocean.com/community/tutorials/how-to-install-node-js-on-ubuntu-18-04)

- `cd ~`
- `curl -sL https://deb.nodesource.com/setup_10.x -o nodesource_setup.sh`
- `sudo bash nodesource_setup.sh`
- `sudo apt install nodejs`

### Angular Front-end to latest

```
npm install -g @angular/cli@latest
ng update --verbose
ng update @angular/core @angular/cli @angular/cdk @angular/material --allow-dirty
npm install
```

- Eventually, after:  
  `npm update`

#### Angular Front-end to a specific version

```
(ng update @angular/core@12 @angular/cli@12 @angular/cdk @angular/material@12 --allow-dirty --force)
```

### Npm & Dependencies

```
npm cache clean --force
(delete node_modules, dist, and eventually package-lock)
npm list -g
npm install
npm run build
```

----------------------------

## First Time Creation
```
npm init -y 
npm install -g typescript 
node -v 
npm install -g typescript 
tsc -v 
npm install -g @angular/cli@latest ----> IMPORTANT!!!
ng v 
ng new front-end
npm install @angular/material @angular/cdk @angular/animations 
ng build –prod 
npm ls @angular/material
```

### Installing Third Part Dependencies
- moment js
- `ng add @ng-bootstrap/ng-bootstrap`

### Project Custom settings
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

## Uninstall
```
npm uninstall -g @angular/cli 
npm uninstall --save-dev angular-cli 
npm cache clean –force 
npm cache verify	
```

--------------------------

#### IDEA Terminal Bash NPM-NG usage
```
cmd.exe
(npm run ng version)
```
