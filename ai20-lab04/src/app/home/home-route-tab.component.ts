import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-home-tab',
  template: `
    <div style="padding: 25px">
      <h1>Welcome to the Virtual Labs!</h1><br>
      <!--      <img style="margin-left: 75px" src="../../assets/login.png" alt="poli" width="200" height="140">-->
    </div>
  `,
  styles: []
})
export class HomeRouteTabComponent implements OnInit {

  constructor() {
  }

  ngOnInit(): void {
  }

}
