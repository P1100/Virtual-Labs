import {Component, OnInit} from '@angular/core';
import {Course} from '../model/course.model';
import {Title} from '@angular/platform-browser';
import {MatDialog} from '@angular/material/dialog';

const DB_COURSES: Course[] = [
  {id: 1, label: 'Applicazioni Internet', path: 'applicazioni-internet'},
  {id: 2, label: 'Programmazione di sistema', path: 'programmazione-di-sistema'},
  {id: 3, label: 'Mobile development', path: 'mobile-development'}
];

@Component({
  // selector changed from app-root, inserted in index.html!
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  title = 'VirtualLabs';
  courses = DB_COURSES;
  loginOrLogout = 'Login';
  loggedUser = '';

  constructor(private titleService: Title, public dialog: MatDialog) {
    titleService.setTitle(this.title);
  }
  ngOnInit(): void {
    console.log('# HomeController.ngOninit START');
  }
}
