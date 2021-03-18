import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-empty',
  template: `
    <div style="padding: 20px">
      <h1 style="margin-left: 225px">Welcome to Virtual Labs!</h1><br>
      <img style="margin-left: 5px" src="../../assets/VirtualLabs.jpg" alt="image virtual labs" width="800" height="400">
    </div>
  `,
  styles: []
})
export class WelcomeEmptyComponent implements OnInit {
  constructor() {
  }
  ngOnInit(): void {
  }
}
