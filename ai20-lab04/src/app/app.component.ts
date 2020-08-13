import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-root',
  template: '<router-outlet></router-outlet>',
  styleUrls: []
})
export class AppComponent implements OnInit {
  title = 'VirtualLabs_app';

  ngOnInit(): void {
    // console.log('AppComponent.ngOninit (title): ' + this.title);
  }
}
