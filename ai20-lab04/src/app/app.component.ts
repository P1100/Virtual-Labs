import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-root',
  template: '<app-home></app-home>',
  styleUrls: []
})
export class AppComponent implements OnInit {
  title = 'VirtualLabs_app';

  ngOnInit(): void {
    // console.log('AppComponent.ngOninit (title): ' + this.title);
  }
}
