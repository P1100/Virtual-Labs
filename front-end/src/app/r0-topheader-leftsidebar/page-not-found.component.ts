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
      <p style="text-align: center; color: midnightblue; font-weight: bold; font-size: xx-large; text-shadow: 2px 2px red;">Not Found</p>
    </div>
  `,
  styles: []
})
export class PageNotFoundComponent {
  constructor(private router: Router) {
  }
  goHome() {
    this.router.navigateByUrl('/');
  }
}
