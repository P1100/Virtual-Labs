import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['../../_unused/app.component.css']
})
export class AppComponent implements OnInit {
  title = 'VirtualLabs';

  ngOnInit(): void {
    console.log('On init: ' + this.title);
  }
}
