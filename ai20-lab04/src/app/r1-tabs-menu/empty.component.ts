import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-empty',
  template: `
    <div style="padding: 20px">
      <h1 style="margin-left: 225px">Welcome to the Virtual Labs!</h1><br>
      <img style="margin-left: 5px" src="../../assets/VirtualLabs.jpg" alt="poli" width="800" height="400">
    </div>
  `,
  styles: []
})
// TODO: image loaded only if not logged? subject su auth service, variabile isLogged?
export class EmptyComponent implements OnInit {
  constructor() {
  }
  ngOnInit(): void {
  }
}
