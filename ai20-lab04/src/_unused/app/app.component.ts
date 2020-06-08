import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'VirtualLabs_app';

  ngOnInit(): void {
    console.log('On init: ' + this.title);
  }
}
