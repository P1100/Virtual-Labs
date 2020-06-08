import {enableProdMode} from '@angular/core';
import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';

import {MainModule} from './app/main.module';
import {environment} from './environments/environment';

if (environment.production) {
  enableProdMode();
}

// TODO: add command for json server    "start": "start npm run init-assets && start npm run init-server"
platformBrowserDynamic().bootstrapModule(MainModule)
  .catch(err => console.error(err));
