import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-home-tab',
  template: `
    <div>
      <img src="../../assets/poli.jpg" alt="poli" width="800" height="450">
    </div>
  `,
  styles: []
})
export class HomeTabComponent implements OnInit {

  constructor() {
  }

  ngOnInit(): void {
  }

}
