import {Component} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-page-not-found',
  template: `
    <div>
      <p style="text-align: center; color: red; font-weight: bold; font-size: xx-large; margin: 30px;">UNAUTHORIZED</p>
    </div>
  `,
  styles: []
})
export class UnauthorizedTabComponent {
  constructor(private router: Router) {
  }
  goHome() {
    this.router.navigateByUrl('/');
  }
}
