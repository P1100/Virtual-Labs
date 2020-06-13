import {enableProdMode} from '@angular/core';
import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';

import {AppModule} from './app/app.module';
import {environment} from './environments/environment';

if (environment.production) {
  enableProdMode();
}

// TODO: add command for json server    "start": "start npm run init-assets && start npm run init-server"
platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.error(err));
