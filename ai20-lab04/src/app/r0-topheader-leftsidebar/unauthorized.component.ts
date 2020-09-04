import {Component} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-page-not-found',
  template: `
    <div style="background-image: url('../../assets/VirtualLabs.jpg');
    margin: 0;
    padding: 20px;
    background-size: cover;
    /*min-height: 500px;*/
    height: 100%;
    /*text-indent: -9999px;*/
    z-index:-1;">
      <button mat-flat-button color="primary" (click)="goHome()" class="alert-primary">HOME</button>
      <p style="text-align: center; color: red; font-weight: bold; font-size: xx-large">UNAUTHORIZED</p>
    </div>
  `,
  styles: []
})
export class UnauthorizedComponent {
  constructor(private router: Router) {
  }
  goHome() {
    this.router.navigateByUrl('/');
  }
}
